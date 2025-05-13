package com.example.wrddbanksystem.Controllers.Client;

import com.example.wrddbanksystem.Models.ApiClient;
import com.example.wrddbanksystem.Models.Client;
import com.example.wrddbanksystem.Core.Model;
import javafx.beans.binding.Bindings;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileController {
    public Text welcome_text;
    public TextField firstName_fld;
    public TextField lastName_fld;
    public TextField payeeAddress_fld;
    public TextField dateCreated_fld;
    public Button updateInfo_btn;

    public PasswordField currentPassword_fld;
    public PasswordField newPassword_fld;
    public PasswordField confirmPassword_fld;
    public Button changePassword_btn;

    public Button deleteAccount_btn;
    public Label status_lbl;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM dd, yyyy");

    //----- Initialize The Profile Controller -----
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadClientData();
        setListeners();
        clearStatusMessage();
    }

    //----- Method To Load Client Data -----
    private void loadClientData() {
        Client client = Model.getInstance().getClient();

        //----- Unbind Any Previous Bindings -----
        if (welcome_text.textProperty().isBound()) {
            welcome_text.textProperty().unbind();
        }

        //----- Set Welcome Message With Client Name -----
        welcome_text.textProperty().bind(
                Bindings.concat("Welcome, ")
                        .concat(client.firstNameProperty())
                        .concat(" ")
                        .concat(client.lastNameProperty())
        );

        // Populate Fields With Client Data
        firstName_fld.setText(client.getFirstName());
        lastName_fld.setText(client.getLastName());
        payeeAddress_fld.setText(client.getPayeeAddress());

        if (client.getDateCreated() != null) {
            dateCreated_fld.setText(client.getDateCreated().format(DATE_FORMATTER));
        } else {
            dateCreated_fld.setText("N/A");
        }

        //----- Clear Password Fields For Security -----
        clearPasswordFields();
    }

    //----- Method To Set Listeners -----
    private void setListeners() {
        updateInfo_btn.setOnAction(event -> updateClientInfo());
        changePassword_btn.setOnAction(event -> changePassword());
        deleteAccount_btn.setOnAction(event -> deleteAccount());
    }

    //----- Method To Update Client Info -----
    private void updateClientInfo() {
        String firstName = firstName_fld.getText().trim();
        String lastName = lastName_fld.getText().trim();

        //----- Validate Inputs -----
        if (firstName.isEmpty() || lastName.isEmpty()) {
            showStatusMessage("Please fill in all required fields", true);
            return;
        }

        //----- Update Client Info Using API -----
        try {
            Map<String, String> clientData = new HashMap<>();
            clientData.put("firstName", firstName);
            clientData.put("lastName", lastName);

            String payeeAddress = Model.getInstance().getClient().getPayeeAddress();
            ApiClient.put("/clients/" + payeeAddress, clientData, Map.class);

            //----- Update Client Model -----
            Model.getInstance().getClient().firstNameProperty().set(firstName);
            Model.getInstance().getClient().lastNameProperty().set(lastName);

            showStatusMessage("Profile updated successfully", false);
        } catch (Exception e) {
            showStatusMessage("Error: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    //----- Method To Change Password -----
    private void changePassword() {
        String currentPassword = currentPassword_fld.getText();
        String newPassword = newPassword_fld.getText();
        String confirmPassword = confirmPassword_fld.getText();

        //----- Validate Inputs -----
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showStatusMessage("Please fill in all password fields", true);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showStatusMessage("New passwords do not match", true);
            return;
        }

        if (newPassword.length() < 6) {
            showStatusMessage("New password must be at least 6 characters", true);
            return;
        }

        //----- Verify Current Password And Update -----
        try {
            String payeeAddress = Model.getInstance().getClient().getPayeeAddress();

            //----- First Verify Current Password -----
            Map<String, String> verifyData = new HashMap<>();
            verifyData.put("payeeAddress", payeeAddress);
            verifyData.put("password", currentPassword);

            Map<String, Object> verifyResult = ApiClient.post("/clients/auth", verifyData, Map.class);

            if (verifyResult == null) {
                showStatusMessage("Current password is incorrect", true);
                return;
            }

            //----- Now Update Password -----
            Map<String, String> passwordData = new HashMap<>();
            passwordData.put("currentPassword", currentPassword);
            passwordData.put("newPassword", newPassword);

            ApiClient.put("/clients/" + payeeAddress + "/password", passwordData, Map.class);
            showStatusMessage("Password updated successfully", false);
            clearPasswordFields();
        } catch (IOException | InterruptedException e) {
            showStatusMessage("Error: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    //----- Method To Delete Account -----
    private void deleteAccount() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action cannot be undone. All your data will be permanently deleted.");

        ButtonType deleteButton = new ButtonType("Delete Account");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(deleteButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == deleteButton) {
            try {
                String payeeAddress = Model.getInstance().getClient().getPayeeAddress();
                ApiClient.delete("/clients/" + payeeAddress, Void.class);

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Account Deleted");
                successAlert.setHeaderText("Your account has been deleted");
                successAlert.setContentText("Thank you for using Wrdd Bank System. Your account has been successfully deleted.");
                successAlert.showAndWait();

                Stage stage = (Stage) deleteAccount_btn.getScene().getWindow();
                Model.getInstance().getViewFactory().closeStage(stage);

                Model.getInstance().setClientLoginSuccessFlag(false);
                Model.getInstance().getViewFactory().showLoginWindow();
            } catch (Exception e) {
                showStatusMessage("Error: " + e.getMessage(), true);
                e.printStackTrace();
            }
        }
    }

    //----- Method To Clear Password Fields -----
    private void clearPasswordFields() {
        currentPassword_fld.clear();
        newPassword_fld.clear();
        confirmPassword_fld.clear();
    }

    //----- Method To Clear Status Message -----
    private void clearStatusMessage() {
        status_lbl.setText("");
    }

    //----- Method To Show Status Message -----
    private void showStatusMessage(String message, boolean isError) {
        status_lbl.setText(message);

        if (isError) {
            status_lbl.setTextFill(Color.RED);
            status_lbl.setStyle("-fx-text-fill: red;");
        } else {
            status_lbl.setTextFill(Color.GREEN);
            status_lbl.setStyle("-fx-text-fill: green;");
        }
    }

    //----- Method To Refresh Profile Data -----
    public void refreshData() {
        try {
            //----- Refresh Client Data From API -----
            String payeeAddress = Model.getInstance().getClient().getPayeeAddress();
            Map<String, Object> clientData = ApiClient.get("/clients/" + payeeAddress, Map.class);

            if (clientData != null) {
                //----- Update Client Model With Fresh Data -----
                Model.getInstance().getClient().firstNameProperty().set((String) clientData.get("firstName"));
                Model.getInstance().getClient().lastNameProperty().set((String) clientData.get("lastName"));
            }

            loadClientData();
            clearStatusMessage();
        } catch (IOException | InterruptedException e) {
            showStatusMessage("Error refreshing data: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }
} 
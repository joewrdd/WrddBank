package com.example.wrddbanksystem.Controllers;

import com.example.wrddbanksystem.Core.Model;
import com.example.wrddbanksystem.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    public ChoiceBox<AccountType> acc_selector;
    public Label payee_address_lbl;
    public TextField payee_address_field;
    public Label password_lbl;
    public TextField password_field;
    public Button login_btn;
    public Label error_lbl;

    //----- Initialize The Login Controller -----
    @FXML
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        acc_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN));
        acc_selector.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        acc_selector.valueProperty().addListener(observable -> setAcc_selector());
        login_btn.setOnAction(event -> onLogin());
    }

    //----- Method To Handle The Login Button Click -----
    private void onLogin() {
        Stage stage = (Stage) error_lbl.getScene().getWindow();

        if (Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.CLIENT) {
            String payeeAddress = payee_address_field.getText();
            String password = password_field.getText();

            //----- Show Loading Indicator Or Disable Login Button -----
            login_btn.setDisable(true);
            error_lbl.setText("Logging in...");
            error_lbl.setVisible(true);

            try {
                //----- Evaluate Client Login Credentials Using API -----
                Model.getInstance().evaluateClientCredentials(payeeAddress, password);

                if (Model.getInstance().isClientLoginSuccessFlag()) {
                    //----- Login Successful -----
                    Model.getInstance().getViewFactory().showClientWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);
                } else {
                    //----- Login Failed -----
                    payee_address_field.clear();
                    password_field.clear();
                    error_lbl.setText("Invalid login credentials. Please try again!");
                    error_lbl.setVisible(true);
                }
            } catch (Exception e) {
                //----- Handle Connection Errors -----
                error_lbl.setText("Connection error: " + e.getMessage());
                error_lbl.setVisible(true);
                System.err.println("Login error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                login_btn.setDisable(false);
            }
        } else {
            //----- Admin Login -----
            String username = payee_address_field.getText();
            String password = password_field.getText();

            //----- Show Loading Indicator -----
            login_btn.setDisable(true);
            error_lbl.setText("Logging in...");
            error_lbl.setVisible(true);

            try {
                //----- Evaluate Admin Login Credentials Using API -----
                Model.getInstance().evaluateAdminCredentials(username, password);

                if (Model.getInstance().getAdminLoginSuccessFlag()) {
                    //----- Login Successful -----
                    Model.getInstance().getViewFactory().showAdminWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);
                } else {
                    //----- Login Failed -----
                    payee_address_field.clear();
                    password_field.clear();
                    error_lbl.setText("Invalid admin credentials. Please try again!");
                    error_lbl.setVisible(true);
                }
            } catch (Exception e) {
                //----- Handle Connection Errors -----
                error_lbl.setText("Connection error: " + e.getMessage());
                error_lbl.setVisible(true);
                System.err.println("Login error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                login_btn.setDisable(false);
            }
        }
    }

    //----- Method To Set The Account Selector -----
    private void setAcc_selector() {
        Model.getInstance().getViewFactory().setLoginAccountType(acc_selector.getValue());
        // Change First Label In Login Depending On Account Type
        if (acc_selector.getValue() == AccountType.ADMIN) {
            payee_address_lbl.setText("Username:");
        } else {
            payee_address_lbl.setText("Payee Address:");
        }
    }
}

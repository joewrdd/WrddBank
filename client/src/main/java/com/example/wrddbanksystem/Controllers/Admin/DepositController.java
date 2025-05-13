package com.example.wrddbanksystem.Controllers.Admin;

import com.example.wrddbanksystem.Models.Client;
import com.example.wrddbanksystem.Core.Model;
import com.example.wrddbanksystem.Views.ClientCellFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DepositController {
    public TextField pAddress_fld;
    public Button search_btn;
    public ListView<Client> result_listview;
    public TextField amount_fld;
    public Button deposit_btn;

    private Client client;

    //----- Initialize The Deposit Controller -----
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deposit_btn.setDisable(true);
        search_btn.setOnAction(e -> onClientSearch());
        deposit_btn.setOnAction(e -> onClientDeposit());
    }

    //----- Method To Search For Client -----
    private void onClientSearch() {
        ObservableList<Client> searchResults = Model.getInstance().searchClient(pAddress_fld.getText());
        result_listview.setItems(searchResults);
        result_listview.setCellFactory(e -> new ClientCellFactory());

        if (!searchResults.isEmpty()) {
            client = searchResults.getFirst();
            deposit_btn.setDisable(false);
        } else {
            client = null;
            deposit_btn.setDisable(true);
            showErrorAlert("Client Not Found", "No client found with payee address: " + pAddress_fld.getText());
        }
    }

    //----- Method To Deposit Savings -----
    private void onClientDeposit() {
        if (client == null || amount_fld.getText() == null || amount_fld.getText().isEmpty()) {
            showErrorAlert("Invalid Input", "Please select a client and enter a valid amount.");
            return;
        }

        try {
            double amount = Double.parseDouble(amount_fld.getText());

            if (amount <= 0) {
                showErrorAlert("Invalid Amount", "Amount must be greater than zero.");
                return;
            }

            String payeeAddress = client.payeeAddressProperty().get();
            String accountNumber = "";

            if (client.savingsAccountProperty().get() != null) {
                accountNumber = client.savingsAccountProperty().get().accountNumberProperty().get();
            } else {
                showErrorAlert("No Account", "Client does not have a savings account.");
                return;
            }

            //----- Use The AdminService Through Model To Create The Deposit -----
            boolean success = Model.getInstance().createDeposit(
                    payeeAddress,
                    accountNumber,
                    "savings",
                    amount,
                    "Admin deposit"
            );

            if (success) {
                showSuccessAlert("Deposit Successful",
                        String.format("$%.2f has been deposited to %s's savings account.",
                                amount, client.firstNameProperty().get()));
                resetFields();

                result_listview.getItems().clear();
            } else {
                showErrorAlert("Deposit Failed", "The deposit could not be processed. Please try again.");
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Format", "Please enter a valid number.");
        } catch (Exception e) {
            showErrorAlert("Error", "An error occurred while processing the deposit: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //----- Method To Reset The Fields -----
    private void resetFields() {
        pAddress_fld.setText("");
        amount_fld.setText("");
    }

    //----- Method To Show Error Alert -----
    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    //----- Method To Show Success Alert -----
    private void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

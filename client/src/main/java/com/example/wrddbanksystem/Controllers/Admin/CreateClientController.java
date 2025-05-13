package com.example.wrddbanksystem.Controllers.Admin;

import com.example.wrddbanksystem.Core.Model;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

//----- Initialize The Create Client Controller -----
public class CreateClientController {
    public TextField fName_fld;
    public TextField lName_fld;
    public TextField password_fld;
    public CheckBox pAddress_box;
    public Label pAddress_lbl;

    //----- Checking Account Fields -----
    public CheckBox ch_acc_checkbox;
    public TextField ch_amount_fld;
    public TextField ch_monthly_fee_fld;
    public CheckBox ch_salary_domiciliation;

    //----- Savings Account Fields -----
    public CheckBox sv_acc_checkbox;
    public TextField sv_amount_fld;
    public TextField sv_interest_rate_fld;
    public ComboBox<String> sv_compounding_frequency;
    public TextField sv_goal_amount_fld;

    public Button create_client_btn;
    public Label error_lbl;

    private String payeeAddress;
    private boolean createCheckingAccountFlag = false;
    private boolean createSavingsAccountFlag = false;

    //----- Initialize The Create Client Controller -----
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ch_monthly_fee_fld.setText("0.00");
        sv_interest_rate_fld.setText("1.50");
        sv_goal_amount_fld.setText("0.00");

        sv_compounding_frequency.setItems(FXCollections.observableArrayList("daily", "monthly", "annually"));
        sv_compounding_frequency.setValue("monthly");

        create_client_btn.setOnAction(event -> createClient());
        pAddress_box.selectedProperty().addListener((observable, oldValue,
                                                     newValue) -> {
            if (newValue) {
                payeeAddress = createPayeeAddress();
                onCreatePayeeAddress();
            }
        });
        ch_acc_checkbox.selectedProperty().addListener((observable, oldValue,
                                                        newValue) -> {
            if (newValue) {
                createCheckingAccountFlag = true;
            }
        });
        sv_acc_checkbox.selectedProperty().addListener((observable, oldValue,
                                                        newValue) -> {
            if (newValue) {
                createSavingsAccountFlag = true;
            }
        });
    }

    //----- Method To Create A Client -----
    private void createClient() {
        if (!validateInputFields()) {
            return;
        }

        try {
            //----- Get Client Data -----
            String fName = fName_fld.getText();
            String lName = lName_fld.getText();
            String password = password_fld.getText();

            //----- Get Account Data -----
            double checkingBalance = createCheckingAccountFlag ? Double.parseDouble(ch_amount_fld.getText()) : 0;
            double savingsBalance = createSavingsAccountFlag ? Double.parseDouble(sv_amount_fld.getText()) : 0;

            //----- Create Client With Accounts -----
            boolean success = Model.getInstance().createClient(
                    fName, lName, payeeAddress, password, checkingBalance, savingsBalance
            );

            if (success) {
                //----- Update UI -----
                error_lbl.setStyle("-fx-text-fill: green; -fx-font-size: 1.3em; -fx-font-weight: bold;");
                error_lbl.setText("Client Created Successfully!");
                error_lbl.setVisible(true);

                //----- Update Accounts With Additional Properties if Needed -----
                if (createCheckingAccountFlag) {
                    updateCheckingAccountProperties(payeeAddress);
                }

                if (createSavingsAccountFlag) {
                    updateSavingsAccountProperties(payeeAddress);
                }

                //----- Refresh Client List -----
                Model.getInstance().setClients();

                //----- Show Success Dialog -----
                Model.getInstance().getViewFactory().showDialogMessage(
                        "Success",
                        "Client " + fName + " " + lName + " has been created successfully with payee address: " + payeeAddress
                );

                resetFields();
            } else {
                //----- Show Error Dialog -----
                error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.3em; -fx-font-weight: bold;");
                error_lbl.setText("Failed to create client!");
                error_lbl.setVisible(true);
            }
        } catch (Exception e) {
            //----- Show Error Dialog -----
            error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.3em; -fx-font-weight: bold;");
            error_lbl.setText("Error: " + e.getMessage());
            error_lbl.setVisible(true);
            e.printStackTrace();
        }
    }

    //----- Method To Validate The Input Fields -----
    private boolean validateInputFields() {
        //----- Validate The Input Fields -----
        if (fName_fld.getText().isEmpty() || lName_fld.getText().isEmpty() || password_fld.getText().isEmpty()) {
            error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.3em; -fx-font-weight: bold;");
            error_lbl.setText("Please fill all required fields!");
            return false;
        }

        //----- Validate The Payee Address -----
        if (payeeAddress == null || payeeAddress.isEmpty()) {
            error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.3em; -fx-font-weight: bold;");
            error_lbl.setText("Please create a payee address!");
            return false;
        }

        //----- Validate The Checking Account -----
        if (createCheckingAccountFlag) {
            try {
                double amount = Double.parseDouble(ch_amount_fld.getText());
                double monthlyFee = Double.parseDouble(ch_monthly_fee_fld.getText());

                if (amount < 0 || monthlyFee < 0) {
                    error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.3em; -fx-font-weight: bold;");
                    error_lbl.setText("Checking account values must be positive!");
                    return false;
                }
            } catch (NumberFormatException e) {
                error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.3em; -fx-font-weight: bold;");
                error_lbl.setText("Invalid checking account values!");
                return false;
            }
        }

        //----- Validate The Savings Account -----
        if (createSavingsAccountFlag) {
            try {
                double amount = Double.parseDouble(sv_amount_fld.getText());
                double interestRate = Double.parseDouble(sv_interest_rate_fld.getText());
                double goalAmount = Double.parseDouble(sv_goal_amount_fld.getText());

                if (amount < 0 || interestRate < 0 || goalAmount < 0) {
                    error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.3em; -fx-font-weight: bold;");
                    error_lbl.setText("Savings account values must be positive!");
                    return false;
                }

                if (interestRate > 10.0) {
                    error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.3em; -fx-font-weight: bold;");
                    error_lbl.setText("Interest rate cannot exceed 10%!");
                    return false;
                }
            } catch (NumberFormatException e) {
                error_lbl.setStyle("-fx-text-fill: red; -fx-font-size: 1.3em; -fx-font-weight: bold;");
                error_lbl.setText("Invalid savings account values!");
                return false;
            }
        }

        return true;
    }

    //----- Method To Update Checking Account Properties -----
    private void updateCheckingAccountProperties(String payeeAddress) {
        try {
            //----- Get Account Data -----
            double monthlyFee = Double.parseDouble(ch_monthly_fee_fld.getText());
            boolean hasSalaryDomiciliation = ch_salary_domiciliation.isSelected();

            /* TODO:  Model.getInstance().updateCheckingAccountProperties(accountNumber, monthlyFee, hasSalaryDomiciliation, "active"); Note: This would require an additional API endpoint and implementation For now, this is handled by the default values in the account creation */
        } catch (Exception e) {
            System.err.println("Error updating checking account properties: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //----- Method To Update Savings Account Properties -----
    private void updateSavingsAccountProperties(String payeeAddress) {
        try {
            //----- Get Account Data -----
            double interestRate = Double.parseDouble(sv_interest_rate_fld.getText());
            String compoundingFrequency = sv_compounding_frequency.getValue();
            double goalAmount = Double.parseDouble(sv_goal_amount_fld.getText());
            
            /* TODO:  Model.getInstance().updateSavingsAccountProperties(accountNumber, interestRate, compoundingFrequency, goalAmount, "active");
            Note: This would require an additional API endpoint and implementation
            For now, this is handled by the default values in the account creation */
        } catch (Exception e) {
            System.err.println("Error updating savings account properties: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //----- Method To Handle Payee Address Creation -----
    private void onCreatePayeeAddress() {
        pAddress_lbl.setText(payeeAddress);
    }

    //----- Method To Create A Payee Address -----
    private String createPayeeAddress() {
        //----- Generate A Random Payee Address With The Format "@XXX-XXXX-XXXX" -----
        int firstSection = new Random().nextInt(900) + 100;
        int secondSection = new Random().nextInt(9000) + 1000;
        int thirdSection = new Random().nextInt(9000) + 1000;
        return "@" + firstSection + "-" + secondSection + "-" + thirdSection;
    }

    //----- Method To Reset Fields -----
    private void resetFields() {
        fName_fld.clear();
        lName_fld.clear();
        password_fld.clear();

        //----- Reset Payee Address -----
        pAddress_box.setSelected(false);
        payeeAddress = null;
        pAddress_lbl.setText("Not Set");

        //----- Reset Checking Account Fields -----
        ch_acc_checkbox.setSelected(false);
        ch_amount_fld.clear();
        ch_monthly_fee_fld.setText("0.00");
        ch_salary_domiciliation.setSelected(false);
        createCheckingAccountFlag = false;

        //----- Reset Savings Account Fields -----
        sv_acc_checkbox.setSelected(false);
        sv_amount_fld.clear();
        sv_interest_rate_fld.setText("1.50");
        sv_compounding_frequency.setValue("monthly");
        sv_goal_amount_fld.setText("0.00");
        createSavingsAccountFlag = false;

        //----- Hide Error Label -----
        error_lbl.setVisible(false);
    }
}

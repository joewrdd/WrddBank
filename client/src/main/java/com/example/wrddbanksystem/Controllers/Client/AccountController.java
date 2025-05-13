package com.example.wrddbanksystem.Controllers.Client;

import com.example.wrddbanksystem.Models.CheckingAccounts;
import com.example.wrddbanksystem.Core.Model;
import com.example.wrddbanksystem.Models.SavingsAccounts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

//----- Account Controller Class -----
public class AccountController {
    public Label ch_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_bal;
    public Label sv_acc_num;
    public Label withdrawal_limit;
    public Label sv_acc_date;
    public Label sv_acc_bal;
    public TextField amount_to_sv;
    public Button trans_to_sv_btn;
    public TextField amount_to_ch;
    public Button trans_to_ch_btn;

    public Label ch_last_transaction;
    public Label ch_monthly_fee;
    public Label ch_salary_domiciliation;
    public Label ch_status;

    public Label sv_interest_rate;
    public Label sv_compounding;
    public Label sv_goal;
    public Label sv_status;

    public TextField sv_goal_amount;
    public Button set_goal_btn;
    public TextField project_months;
    public Button project_btn;
    public Label projected_balance;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    //----- Initialize The Account Controller -----
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        addListeners();
    }

    //----- Method To Bind Data -----
    private void bindData() {
        // Bind Checking Account Data
        CheckingAccounts checkingAccount = (CheckingAccounts) Model.getInstance().getClient().checkingAccountProperty().get();
        if (checkingAccount != null) {
            // Use direct text setting instead of binding
            ch_acc_num.setText(checkingAccount.getAccountNumber());
            transaction_limit.setText(String.valueOf(checkingAccount.getTransactionLimit()));
            ch_acc_bal.setText("$" + checkingAccount.getBalance());
            ch_acc_date.setText("" + LocalDate.now());

            if (checkingAccount.getLastTransactionDate() != null) {
                ch_last_transaction.setText(checkingAccount.getLastTransactionDate().format(DATE_FORMATTER));
            } else {
                ch_last_transaction.setText("Never");
            }

            ch_monthly_fee.setText("$" + checkingAccount.getMonthlyFee());
            ch_salary_domiciliation.setText(checkingAccount.hasSalaryDomiciliation() ? "Yes" : "No");
            ch_status.setText(checkingAccount.getStatus().toString());
        }

        //----- Bind Savings Account Data -----
        SavingsAccounts savingsAccount = (SavingsAccounts) Model.getInstance().getClient().savingsAccountProperty().get();
        if (savingsAccount != null) {
            //----- Use Direct Text Setting Instead Of Binding -----
            sv_acc_num.setText(savingsAccount.getAccountNumber());
            withdrawal_limit.setText("$" + savingsAccount.getWithdrawalLimit());
            sv_acc_bal.setText("$" + savingsAccount.getBalance());
            sv_acc_date.setText("" + LocalDate.now());

            sv_interest_rate.setText(savingsAccount.getInterestRate() + "%");
            sv_compounding.setText(savingsAccount.getCompoundingFrequency().toString());
            sv_goal.setText("$" + savingsAccount.getGoalAmount());
            sv_status.setText(savingsAccount.getStatus().toString());
        }

        //----- Set Projected Balance Initially -----
        projected_balance.setText("$0.00");
    }

    //----- Method To Refresh Account Data -----
    public void refreshAccountData() {
        //----- First Refresh Accounts From The Server -----
        Model.getInstance().refreshAccounts();

        //----- Then Update UI With Fresh Data -----
        bindData();
    }

    //----- Method To Add Listeners -----
    private void addListeners() {
        trans_to_sv_btn.setOnAction(e -> transferToSavings());
        trans_to_ch_btn.setOnAction(e -> transferToChecking());
        set_goal_btn.setOnAction(e -> setSavingsGoal());
        project_btn.setOnAction(e -> calculateInterestProjection());
    }

    //----- Method To Set Savings Goal -----
    private void setSavingsGoal() {
        try {
            double goalAmount = Double.parseDouble(sv_goal_amount.getText());
            String clientPayeeAddress = Model.getInstance().getClient().payeeAddressProperty().get();
            SavingsAccounts savingsAccount = (SavingsAccounts) Model.getInstance().getClient().savingsAccountProperty().get();

            if (savingsAccount != null) {
                if (goalAmount <= 0) {
                    showErrorAlert("Invalid Amount", "Please enter a positive goal amount.");
                    return;
                }

                savingsAccount.setGoalAmount(goalAmount);

                // Use service layer to update account properties
                boolean success = Model.getInstance().getAccountService().updateSavingsAccount(
                        savingsAccount.accountNumberProperty().get(),
                        savingsAccount.withdrawalLimitProperty().get(),
                        savingsAccount.interestRateProperty().get(),
                        savingsAccount.getCompoundingFrequency().toString(),
                        goalAmount
                );

                if (success) {
                    sv_goal_amount.clear();
                    showSuccessAlert("Goal Set", "Your savings goal has been set to $" + goalAmount);
                } else {
                    showErrorAlert("Update Failed", "Could not update savings goal. Please try again.");
                }
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Input", "Please enter a valid number for the goal amount.");
        } catch (Exception e) {
            showErrorAlert("Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //----- Method To Calculate Interest Projection -----
    private void calculateInterestProjection() {
        try {
            int months = Integer.parseInt(project_months.getText());
            SavingsAccounts savingsAccount = (SavingsAccounts) Model.getInstance().getClient().savingsAccountProperty().get();

            if (savingsAccount != null) {
                if (months <= 0 || months > 360) {
                    showErrorAlert("Invalid Input", "Please enter a number of months between 1 and 360.");
                    return;
                }

                double projectedAmount = savingsAccount.projectBalance(months);

                projected_balance.setText(String.format("$%.2f", projectedAmount));

                project_months.clear();
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Input", "Please enter a valid number of months.");
        }
    }

    //----- Method To Transfer To Savings -----
    private void transferToSavings() {
        //----- Try To Transfer To Savings -----
        try {
            double amount = Double.parseDouble(amount_to_sv.getText());
            String clientPayeeAddress = Model.getInstance().getClient().payeeAddressProperty().get();

            CheckingAccounts checkingAccount = (CheckingAccounts) Model.getInstance().getClient().checkingAccountProperty().get();
            SavingsAccounts savingsAccount = (SavingsAccounts) Model.getInstance().getClient().savingsAccountProperty().get();

            //----- Check If Accounts Are Valid -----
            if (checkingAccount != null && savingsAccount != null) {
                if (!checkingAccount.isActive() || !savingsAccount.isActive()) {
                    showErrorAlert("Account Inactive", "One or both of your accounts is not in active status.");
                    return;
                }

                double currentCheckingBalance = checkingAccount.balanceProperty().get();

                if (amount <= 0) {
                    showErrorAlert("Invalid Amount", "Please enter a positive amount to transfer.");
                    return;
                }

                if (amount > currentCheckingBalance) {
                    showErrorAlert("Insufficient Funds", "You don't have enough funds in your checking account.");
                    return;
                }

                //----- Process The Transfer Via Transaction Service -----
                boolean success = Model.getInstance().createTransaction(
                        clientPayeeAddress,
                        clientPayeeAddress,
                        amount,
                        "Transfer from Checking to Savings",
                        "checking"
                );

                if (success) {
                    //----- Refresh Account Data In UI -----
                    refreshAccountData();

                    //----- Refresh Transaction Lists -----
                    Model.getInstance().setLatestTransactions();
                    Model.getInstance().setAllTransactions();

                    amount_to_sv.clear();
                    showSuccessAlert("Transfer Complete", "Successfully transferred $" + amount + " to your savings account!");
                } else {
                    showErrorAlert("Transfer Failed", "The transfer could not be completed. Please try again.");
                }
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Input", "Please enter a valid number.");
        } catch (Exception e) {
            showErrorAlert("Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //----- Method To Transfer To Checking -----
    private void transferToChecking() {
        try {
            double amount = Double.parseDouble(amount_to_ch.getText());
            String clientPayeeAddress = Model.getInstance().getClient().payeeAddressProperty().get();

            CheckingAccounts checkingAccount = (CheckingAccounts) Model.getInstance().getClient().checkingAccountProperty().get();
            SavingsAccounts savingsAccount = (SavingsAccounts) Model.getInstance().getClient().savingsAccountProperty().get();

            if (checkingAccount != null && savingsAccount != null) {
                if (!checkingAccount.isActive() || !savingsAccount.isActive()) {
                    showErrorAlert("Account Inactive", "One or both of your accounts is not in active status.");
                    return;
                }

                double currentSavingsBalance = savingsAccount.balanceProperty().get();

                if (amount <= 0) {
                    showErrorAlert("Invalid Amount", "Please enter a positive amount to transfer.");
                    return;
                }

                if (amount > currentSavingsBalance) {
                    showErrorAlert("Insufficient Funds", "You don't have enough funds in your savings account.");
                    return;
                }

                double withdrawalLimit = savingsAccount.withdrawalLimitProperty().get();
                if (amount > withdrawalLimit) {
                    showErrorAlert("Withdrawal Limit Exceeded",
                            "The amount exceeds your withdrawal limit of $" + withdrawalLimit);
                    return;
                }

                //----- Process The Transfer Via Transaction Service -----
                boolean success = Model.getInstance().createTransaction(
                        clientPayeeAddress,
                        clientPayeeAddress,
                        amount,
                        "Transfer from Savings to Checking",
                        "savings"
                );

                if (success) {
                    //----- Refresh Account Data In UI -----
                    refreshAccountData();

                    // Refresh transaction lists
                    Model.getInstance().setLatestTransactions();
                    Model.getInstance().setAllTransactions();

                    amount_to_ch.clear();
                    showSuccessAlert("Transfer Complete", "Successfully transferred $" + amount + " to your checking account!");
                } else {
                    showErrorAlert("Transfer Failed", "The transfer could not be completed. Please try again.");
                }
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Input", "Please enter a valid number.");
        } catch (Exception e) {
            showErrorAlert("Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
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

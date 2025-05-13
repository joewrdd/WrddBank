package com.example.wrddbanksystem.Controllers.Client;

import com.example.wrddbanksystem.Core.Model;
import com.example.wrddbanksystem.Models.*;
import com.example.wrddbanksystem.Views.TransactionCellFactory;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController {
    public Text user_name;
    public Label login_date;
    public Label checking_balance;
    public Label checking_acc_num;
    public Label savings_balance;
    public Label savings_acc_num;
    public Label income_lbl;
    public Label expense_lbl;
    public ListView<Transaction> transaction_listview;
    public TextField payee_address_fld;
    public TextField amount_fld;
    public TextArea message_fld;
    public Button send_money_btn;
    public ComboBox<String> source_account_selector;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        //----- Always Refresh Accounts To Get The Most Recent Data -----
        Model.getInstance().refreshAccounts();

        bindData();
        loadLatestTransactions();
        setupSourceAccountSelector();

        transaction_listview.setCellFactory(e -> new TransactionCellFactory());
        send_money_btn.setOnAction(e -> sendMoney());
        accountSummary();
    }

    //----- Method Sets Up The Source Account Selector -----
    private void setupSourceAccountSelector() {
        source_account_selector.getItems().addAll("Checking Account", "Savings Account");
        source_account_selector.setValue("Checking Account"); // Default to checking
    }

    //----- Method Binds The Data To The UI -----
    private void bindData() {
        //----- Get The Client -----
        Client client = Model.getInstance().getClient();

        //----- Instead Of Binding, Directly Set Text To Prevent Stale Data Issues -----
        user_name.setText(client.getFirstName() + " " + client.getLastName());

        //----- Format And Set The Login Date -----
        login_date.setText("Today, " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM, yyyy")));

        //----- Directly Set Checking Account Information -----
        if (client.getCheckingAccount() != null) {
            checking_balance.setText("$" + client.getCheckingAccount().getBalance());
            checking_acc_num.setText(client.getCheckingAccount().getAccountNumber());
        } else {
            checking_balance.setText("$0.00");
            checking_acc_num.setText("N/A");
        }

        //----- Directly Set Savings Account Information -----
        if (client.getSavingsAccount() != null) {
            savings_balance.setText("$" + client.getSavingsAccount().getBalance());
            savings_acc_num.setText(client.getSavingsAccount().getAccountNumber());
        } else {
            savings_balance.setText("$0.00");
            savings_acc_num.setText("N/A");
        }
    }

    //----- Method Loads The Latest Transactions -----
    private void loadLatestTransactions() {
        //----- Always Reload Latest Transactions To Get Fresh Data -----
        Model.getInstance().setLatestTransactions();
        transaction_listview.setItems(Model.getInstance().getLatestTransactions());
    }

    //----- Method Sends Money To Another Client -----
    private void sendMoney() {
        try {
            String receiver = payee_address_fld.getText().trim();

            //----- Ensure Consistent Format With @ Symbol -----
            if (!receiver.startsWith("@")) {
                receiver = "@" + receiver;
            }

            double amount = Double.parseDouble(amount_fld.getText());
            String message = message_fld.getText();
            String sender = Model.getInstance().getClient().payeeAddressProperty().get();

            String sourceAccountType = source_account_selector.getValue().startsWith("Checking") ?
                    "checking" : "savings";

            CheckingAccounts checkingAccount = null;
            SavingsAccounts savingsAccount = null;
            double currentBalance = 0;

            if (sourceAccountType.equals("checking")) {
                checkingAccount = (CheckingAccounts) Model.getInstance().getClient().checkingAccountProperty().get();
                if (checkingAccount != null) {
                    currentBalance = checkingAccount.balanceProperty().get();

                    if (amount > checkingAccount.getTransactionLimit()) {
                        showErrorAlert("Transaction Limit Exceeded",
                                "This amount exceeds your transaction limit of $" +
                                        checkingAccount.getTransactionLimit());
                        return;
                    }
                } else {
                    showErrorAlert("No Checking Account", "You don't have a checking account to send money from.");
                    return;
                }
            } else {
                savingsAccount = (SavingsAccounts) Model.getInstance().getClient().savingsAccountProperty().get();
                if (savingsAccount != null) {
                    currentBalance = savingsAccount.balanceProperty().get();

                    if (amount > savingsAccount.getWithdrawalLimit()) {
                        showErrorAlert("Withdrawal Limit Exceeded",
                                "This amount exceeds your withdrawal limit of $" +
                                        savingsAccount.getWithdrawalLimit());
                        return;
                    }
                } else {
                    showErrorAlert("No Savings Account", "You don't have a savings account to send money from.");
                    return;
                }
            }

            //----- Validate Amount -----
            if (amount <= 0) {
                showErrorAlert("Invalid Amount", "Please enter a positive amount to send.");
                return;
            }

            //----- Check Sufficient Balance -----
            if (amount > currentBalance) {
                showErrorAlert("Insufficient Funds",
                        "You don't have enough funds in your " +
                                sourceAccountType + " account.");
                return;
            }

            //----- Verify That The Receiver Exists By Searching For Them -----
            ObservableList<Client> searchResults = Model.getInstance().searchClient(receiver);

            if (!searchResults.isEmpty()) {
                //----- Create Transaction Using The REST API -----
                boolean success = Model.getInstance().createTransaction(sender, receiver, amount, message, sourceAccountType);

                if (success) {
                    //----- Update UI -----
                    Model.getInstance().refreshAccounts();
                    refreshTransactions();

                    //----- Clear Fields -----
                    payee_address_fld.clear();
                    amount_fld.clear();
                    message_fld.clear();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Transaction Successful");
                    alert.setHeaderText("Money Sent");
                    alert.setContentText("$" + amount + " has been sent to " + receiver +
                            " from your " + sourceAccountType + " account.");
                    alert.showAndWait();
                } else {
                    showErrorAlert("Transaction Failed", "There was an error processing your transaction.");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Receiver Not Found");
                alert.setContentText("No account found with address: " + receiver);
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Amount", "Please enter a valid number for the amount.");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    //----- Method Shows An Error Alert -----
    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(content);
        alert.showAndWait();
    }

    //----- Method Refreshes Transactions -----
    private void refreshTransactions() {
        Model.getInstance().setLatestTransactions();
        transaction_listview.setItems(Model.getInstance().getLatestTransactions());
    }

    //----- Method Calculates Account Summary -----
    private void accountSummary() {
        //----- Always Reload All Transactions To Get Fresh Data -----
        Model.getInstance().setAllTransactions();

        //----- Display Income And Expense Using The Helper Methods -----
        double income = Model.getInstance().getSumOfIncome();
        double expense = Model.getInstance().getSumOfExpense();

        income_lbl.setText("+ $" + String.format("%.2f", income));
        expense_lbl.setText("- $" + String.format("%.2f", expense));
    }
}

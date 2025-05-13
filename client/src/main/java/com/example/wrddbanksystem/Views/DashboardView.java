package com.example.wrddbanksystem.Views;

import com.example.wrddbanksystem.Controllers.Client.DashboardController;
import com.example.wrddbanksystem.Models.Transaction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;


//----- Dashboard View -----
public class DashboardView {
    
   
    //----- Create View -----
    public AnchorPane createView() {
        //----- Create Dashboard View Using Pure JavaFX -----
        AnchorPane dashboardView = new AnchorPane();
        dashboardView.setPrefHeight(750.0);
        dashboardView.setPrefWidth(950.0);
        dashboardView.getStyleClass().add("dashboard");
        dashboardView.getStylesheets().add(getClass().getResource("/Styles/dashboard.css").toExternalForm());
        
        //----- Create Dashboard Controller -----
        DashboardController dashboardController = new DashboardController();
        
        //----- Create Username Text -----
        Text userName = new Text("Hi, Joe");
        userName.getStyleClass().add("user_name");
        AnchorPane.setLeftAnchor(userName, 25.0);
        AnchorPane.setTopAnchor(userName, 25.0);
        dashboardController.user_name = userName;
        
        //----- Create Login Date Label -----
        Label loginDate = new Label("Today, 2025-04-04");
        loginDate.getStyleClass().add("date_lbl");
        AnchorPane.setRightAnchor(loginDate, 25.0);
        AnchorPane.setTopAnchor(loginDate, 25.0);
        dashboardController.login_date = loginDate;
        
        //----- Create Account Summary Text -----
        Text accountSummaryText = new Text("Accounts Summary");
        accountSummaryText.getStyleClass().add("section_title");
        AnchorPane.setRightAnchor(accountSummaryText, 25.0);
        AnchorPane.setTopAnchor(accountSummaryText, 75.0);
        
        //----- Create My Accounts Text -----
        Text myAccountsText = new Text("My Accounts");
        myAccountsText.getStyleClass().add("section_title");
        AnchorPane.setLeftAnchor(myAccountsText, 25.0);
        AnchorPane.setTopAnchor(myAccountsText, 75.0);
        
        //----- Create Accounts View Container -----
        HBox accountsView = new HBox(20);
        accountsView.setPrefHeight(170.0);
        accountsView.setPrefWidth(670.0);
        accountsView.getStyleClass().add("accounts_view");
        accountsView.setAlignment(Pos.CENTER_LEFT);
        AnchorPane.setLeftAnchor(accountsView, 25.0);
        AnchorPane.setTopAnchor(accountsView, 105.0);
        
        //----- Create Checking Account Pane -----
        AnchorPane checkingAccount = createCheckingAccountPane(dashboardController);
        
        //----- Create Savings Account Pane -----
        AnchorPane savingsAccount = createSavingsAccountPane(dashboardController);
        
        //----- Add Account Panes To Accounts View -----
        accountsView.getChildren().addAll(checkingAccount, savingsAccount);
        
        //----- Create Summary View -----
        VBox summaryView = createSummaryView(dashboardController);
        
        //----- Create Latest Transactions Text -----
        Text latestTransactionsText = new Text("Latest Transactions");
        latestTransactionsText.getStyleClass().add("section_title");
        AnchorPane.setLeftAnchor(latestTransactionsText, 25.0);
        AnchorPane.setTopAnchor(latestTransactionsText, 285.0);
        
        //----- Create Send Money Text -----
        Text sendMoneyText = new Text("Send Money");
        sendMoneyText.getStyleClass().add("section_title");
        AnchorPane.setRightAnchor(sendMoneyText, 25.0);
        AnchorPane.setTopAnchor(sendMoneyText, 285.0);
        
        //----- Create Transaction List View -----
        ListView<Transaction> transactionListView = createTransactionListView(dashboardController);
        
        //----- Create New Transaction Container -----
        VBox newTransactionContainer = createNewTransactionContainer(dashboardController);
        
        //----- Add All Components To The Dashboard View -----
        dashboardView.getChildren().addAll(
            userName, loginDate,
            accountSummaryText, myAccountsText,
            accountsView, summaryView,
            latestTransactionsText, sendMoneyText,
            transactionListView, newTransactionContainer
        );
        
        //----- Initialize The Controller -----
        dashboardController.initialize(null, null);
        
        return dashboardView;
    }
    
    //----- Create Checking Account Pane -----
    private AnchorPane createCheckingAccountPane(DashboardController dashboardController) {
        //----- Create Checking Account Pane -----
        AnchorPane checkingAccount = new AnchorPane();
        checkingAccount.setPrefHeight(170.0);
        checkingAccount.setPrefWidth(325.0);
        checkingAccount.getStyleClass().addAll("account", "account_checking");
        
        //----- Create Checking Balance Label -----
        Label checkingBalance = new Label("$28000.0");
        checkingBalance.getStyleClass().add("account_balance");
        AnchorPane.setLeftAnchor(checkingBalance, 20.0);
        AnchorPane.setTopAnchor(checkingBalance, 25.0);
        dashboardController.checking_balance = checkingBalance;
        
        //----- Create Account Number Labels For Checking -----
        Label checkingStars = new Label("**** **** ****");
        checkingStars.getStyleClass().add("account_number");
        AnchorPane.setBottomAnchor(checkingStars, 45.0);
        AnchorPane.setLeftAnchor(checkingStars, 20.0);
        
        //----- Create Checking Account Number Label -----
        Label checkingAccNum = new Label("13877024");
        checkingAccNum.getStyleClass().add("account_number");
        AnchorPane.setBottomAnchor(checkingAccNum, 45.0);
        AnchorPane.setLeftAnchor(checkingAccNum, 130.0);
        dashboardController.checking_acc_num = checkingAccNum;
        
        //----- Create Bank Icon For Checking -----
        FontAwesomeIconView checkingBankIcon = new FontAwesomeIconView(FontAwesomeIcon.BANK);
        checkingBankIcon.setSize("28");
        AnchorPane.setRightAnchor(checkingBankIcon, 20.0);
        AnchorPane.setTopAnchor(checkingBankIcon, 15.0);
        
        //----- Create Checking Account Type Text -----
        Text checkingText = new Text("Checking Account");
        AnchorPane.setBottomAnchor(checkingText, 15.0);
        AnchorPane.setLeftAnchor(checkingText, 20.0);
        
        //----- Add Elements To Checking Account Pane -----
        checkingAccount.getChildren().addAll(
            checkingBalance, checkingStars, checkingAccNum, 
            checkingBankIcon, checkingText
        );
        
        return checkingAccount;
    }
    
    //----- Create Savings Account Pane -----
    private AnchorPane createSavingsAccountPane(DashboardController dashboardController) {
        //----- Create Savings Account Pane -----
        AnchorPane savingsAccount = new AnchorPane();
        savingsAccount.setPrefHeight(170.0);
        savingsAccount.setPrefWidth(325.0);
        savingsAccount.getStyleClass().addAll("account", "account_savings");
        
        //----- Create Savings Balance Label -----
        Label savingsBalance = new Label("$62000.0");
        savingsBalance.getStyleClass().add("account_balance");
        AnchorPane.setLeftAnchor(savingsBalance, 20.0);
        AnchorPane.setTopAnchor(savingsBalance, 25.0);
        dashboardController.savings_balance = savingsBalance;
        
        //----- Create Account Number Labels For Savings -----
        Label savingsStars = new Label("**** **** ****");
        savingsStars.getStyleClass().add("account_number");
        AnchorPane.setBottomAnchor(savingsStars, 45.0);
        AnchorPane.setLeftAnchor(savingsStars, 20.0);
        
        Label savingsAccNum = new Label("13878396");
        savingsAccNum.getStyleClass().add("account_number");
        AnchorPane.setBottomAnchor(savingsAccNum, 45.0);
        AnchorPane.setLeftAnchor(savingsAccNum, 130.0);
        dashboardController.savings_acc_num = savingsAccNum;
        
        //----- Create Bank Icon For Savings -----
        FontAwesomeIconView savingsBankIcon = new FontAwesomeIconView(FontAwesomeIcon.BANK);
        savingsBankIcon.setSize("28");
        AnchorPane.setRightAnchor(savingsBankIcon, 20.0);
        AnchorPane.setTopAnchor(savingsBankIcon, 15.0);
        
        //----- Create Savings Account Type Text -----
        Text savingsText = new Text("Savings Account");
        AnchorPane.setBottomAnchor(savingsText, 15.0);
        AnchorPane.setLeftAnchor(savingsText, 20.0);
        
        //----- Add Elements To Savings Account Pane -----
        savingsAccount.getChildren().addAll(
            savingsBalance, savingsStars, savingsAccNum, 
            savingsBankIcon, savingsText
        );
        
        return savingsAccount;
    }
    
    //----- Create Summary View -----
    private VBox createSummaryView(DashboardController dashboardController) {
        //----- Create Summary View -----
        VBox summaryView = new VBox();
        summaryView.setPrefHeight(170.0);
        summaryView.setPrefWidth(200.0);
        summaryView.getStyleClass().add("summary_view");
        summaryView.setAlignment(Pos.CENTER);
        AnchorPane.setRightAnchor(summaryView, 25.0);
        AnchorPane.setTopAnchor(summaryView, 105.0);
        
        //----- Create Account Summary Container -----
        VBox accSummaryContainer = new VBox(8);
        accSummaryContainer.setPrefHeight(150.0);
        accSummaryContainer.setPrefWidth(180.0);
        accSummaryContainer.getStyleClass().add("acc_summary_container");
        accSummaryContainer.setPadding(new Insets(15));
        
        //----- Create Income Section -----
        Text incomeText = new Text("Income");
        incomeText.getStyleClass().add("summary_label");
        
        Label incomeAmount = new Label("+ $11200.0");
        incomeAmount.getStyleClass().add("income_amount");
        dashboardController.income_lbl = incomeAmount;
        
        //----- Create Separator -----
        Line separatorLine = new Line();
        separatorLine.setEndX(150.0);
        separatorLine.setStrokeWidth(1.0);
        separatorLine.getStyleClass().add("separator");
        separatorLine.setTranslateY(5.0);
        
        //----- Create Expenses Section -----
        Text expensesText = new Text("Expenses");
        expensesText.getStyleClass().add("summary_label");
        expensesText.setTranslateY(5.0);
        
        Label expenseAmount = new Label("- $16700.0");
        expenseAmount.getStyleClass().add("expense_amount");
        dashboardController.expense_lbl = expenseAmount;
        
        //----- Add Elements To Account Summary Container -----
        accSummaryContainer.getChildren().addAll(
            incomeText, incomeAmount, separatorLine, 
            expensesText, expenseAmount
        );
        
        //----- Add Account Summary Container To Summary View -----
        summaryView.getChildren().add(accSummaryContainer);
        
        return summaryView;
    }
    
    //----- Create Transaction List View -----
    private ListView<Transaction> createTransactionListView(DashboardController dashboardController) {
        //----- Create Transaction List View -----
        ListView<Transaction> transactionListView = new ListView<>();
        transactionListView.setPrefHeight(650.0);
        transactionListView.setPrefWidth(800.0);
        transactionListView.getStyleClass().add("transaction_listview");
        transactionListView.setFixedCellSize(65.0);
        
        //----- Prevent Horizontal Movement And Make Cells Stable -----
        transactionListView.setMinWidth(800.0);
        transactionListView.setMaxWidth(800.0);
        
        AnchorPane.setBottomAnchor(transactionListView, 25.0);
        AnchorPane.setLeftAnchor(transactionListView, 25.0);
        AnchorPane.setTopAnchor(transactionListView, 315.0);
        dashboardController.transaction_listview = transactionListView;
        
        return transactionListView;
    }
    
    //----- Create New Transaction Container -----
    private VBox createNewTransactionContainer(DashboardController dashboardController) {
        //----- Create New Transaction Container -----
        VBox newTransactionContainer = new VBox(8);
        newTransactionContainer.setPrefHeight(380.0);
        newTransactionContainer.setPrefWidth(230.0);
        newTransactionContainer.getStyleClass().add("new_transaction_container");
        newTransactionContainer.setPadding(new Insets(15, 15, 15, 15));
        AnchorPane.setBottomAnchor(newTransactionContainer, 25.0);
        AnchorPane.setRightAnchor(newTransactionContainer, 25.0);
        AnchorPane.setTopAnchor(newTransactionContainer, 315.0);
        
        //----- Create Payee Address Section -----
        Label payeeAddressLabel = new Label("Payee Address");
        payeeAddressLabel.setStyle("-fx-font-weight: bold;");
        TextField payeeAddressField = new TextField();
        payeeAddressField.setPrefHeight(30.0);
        dashboardController.payee_address_fld = payeeAddressField;
        
        //----- Create Amount Section -----
        Label amountLabel = new Label("Amount in $:");
        amountLabel.setStyle("-fx-font-weight: bold;");
        TextField amountField = new TextField();
        amountField.setPrefHeight(30.0);
        dashboardController.amount_fld = amountField;
        
        //----- Create Message Section -----
        Label messageLabel = new Label("Message: (Optional)");
        messageLabel.setStyle("-fx-font-weight: bold;");
        TextArea messageField = new TextArea();
        messageField.setPrefHeight(80.0);
        messageField.setPrefWidth(200.0);
        dashboardController.message_fld = messageField;
        
        //----- Create Source Account Section -----
        Label sourceAccountLabel = new Label("Source Account:");
        sourceAccountLabel.setStyle("-fx-font-weight: bold;");
        ComboBox<String> sourceAccountSelector = new ComboBox<>();
        sourceAccountSelector.setPrefWidth(200.0);
        sourceAccountSelector.setPrefHeight(30.0);
        dashboardController.source_account_selector = sourceAccountSelector;
        
        //----- Add Some Spacing To Push The Button To The Bottom -----
        Region spacer = new Region();
        spacer.setPrefHeight(10.0);
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        //----- Create Send Money Button -----
        Button sendMoneyBtn = new Button("Send Money");
        sendMoneyBtn.setPrefHeight(40.0);
        sendMoneyBtn.setPrefWidth(200.0);
        sendMoneyBtn.getStyleClass().add("action_btn");
        sendMoneyBtn.setStyle("-fx-background-color: linear-gradient(to right, #003333, #001111); -fx-text-fill: white; -fx-font-weight: bold;");
        dashboardController.send_money_btn = sendMoneyBtn;
        
        //----- Add Elements To New Transaction Container -----
        newTransactionContainer.getChildren().addAll(
            payeeAddressLabel, payeeAddressField,
            amountLabel, amountField,
            messageLabel, messageField,
            sourceAccountLabel, sourceAccountSelector,
            spacer, sendMoneyBtn
        );
        
        return newTransactionContainer;
    }
} 
package com.example.wrddbanksystem.Views;

import com.example.wrddbanksystem.Controllers.Client.AccountController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

//----- Class To Create And Manage The Accounts View For The Client Interface -----
public class AccountsView {
    
    //----- Method To Create The Accounts View With All Its Components -----
    public AnchorPane createView() {
        try {
            //----- Create Accounts View Using Pure JavaFX -----
            AnchorPane accountsView = new AnchorPane();
            accountsView.setPrefHeight(750.0);
            accountsView.setPrefWidth(850.0);
            accountsView.getStyleClass().add("accounts_view_container");
            accountsView.getStylesheets().add(getClass().getResource("/Styles/accounts.css").toExternalForm());

            //----- Create Account Controller -----
            AccountController accountController = new AccountController();

            //----- Create Scroll Pane -----
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.getStyleClass().add("transparent-scroll-pane");
            AnchorPane.setBottomAnchor(scrollPane, 0.0);
            AnchorPane.setLeftAnchor(scrollPane, 0.0);
            AnchorPane.setRightAnchor(scrollPane, 0.0);
            AnchorPane.setTopAnchor(scrollPane, 0.0);

            //----- Create Main Content Container -----
            VBox mainContainer = new VBox(30);
            mainContainer.setStyle("-fx-padding: 20 20 40 20;");

            //----- *** CHECKING ACCOUNT SECTION *** -----
            Text checkingTitle = new Text("Checkings Account");
            checkingTitle.getStyleClass().add("title");

            HBox checkingSection = new HBox(30);

            //----- Left Column - Account Details -----
            VBox checkingDetailsBox = createCheckingDetailsBox(accountController);
            
            //----- Right Column - Transfer Functions -----
            VBox checkingTransferBox = createCheckingTransferBox(accountController);

            //----- Add Both Columns To Checking Section -----
            checkingSection.getChildren().addAll(checkingDetailsBox, checkingTransferBox);

            //----- *** SAVINGS ACCOUNT SECTION *** -----
            Text savingsTitle = new Text("Savings Account");
            savingsTitle.getStyleClass().add("title");

            HBox savingsSection = new HBox(30);

            //----- Left Column - Account Details -----
            VBox savingsDetailsBox = createSavingsDetailsBox(accountController);
            
            //----- Right Column - Transfer Functions -----
            VBox savingsTransferBox = createSavingsTransferBox(accountController);

            //----- Add Both Columns To Savings Section -----
            savingsSection.getChildren().addAll(savingsDetailsBox, savingsTransferBox);

            //----- Add All Sections To Main Container -----
            mainContainer.getChildren().addAll(
                    checkingTitle, checkingSection,
                    savingsTitle, savingsSection
            );

            //----- Set Main Container To Scroll Pane -----
            scrollPane.setContent(mainContainer);

            //----- Add Scroll Pane To Accounts View -----
            accountsView.getChildren().add(scrollPane);

            //----- Initialize Controller -----
            accountController.initialize(null, null);
            
            //----- Store Controller As User Data So It Can Be Accessed Later -----
            accountsView.setUserData(accountController);
            
            //----- Ensure Account Data Is Refreshed -----
            accountController.refreshAccountData();
            
            return accountsView;
            
        } catch (Exception e) {
            e.printStackTrace();
            return new AnchorPane(); // Return Empty Pane In Case Of Error
        }
    }
    
    //----- Method To Create The Checking Account Details Box -----
    private VBox createCheckingDetailsBox(AccountController accountController) {
        //----- Left Column - Account Details -----
        VBox checkingDetailsBox = new VBox(10);
        checkingDetailsBox.setPrefWidth(450.0);
        checkingDetailsBox.getStyleClass().add("account_box");
        checkingDetailsBox.setPadding(new Insets(20, 20, 20, 20));

        //----- Account Number -----
        Text accountNumberText = new Text("Account Number:");
        Label accountNumberLabel = new Label("3452 4950");
        accountController.ch_acc_num = accountNumberLabel;

        //----- Transaction Limit -----
        Text transactionLimitText = new Text("Transaction Limit:");
        Label transactionLimitLabel = new Label("10");
        accountController.transaction_limit = transactionLimitLabel;

        //----- Date Created -----
        Text dateCreatedText = new Text("Date Created:");
        Label dateCreatedLabel = new Label("2025-02-22");
        dateCreatedLabel.setPrefHeight(32.0);
        dateCreatedLabel.setPrefWidth(110.0);
        accountController.ch_acc_date = dateCreatedLabel;

        //----- Balance -----
        Text balanceText = new Text("Balance:");
        Label balanceLabel = new Label("3,000.00");
        accountController.ch_acc_bal = balanceLabel;

        //----- Last Transaction -----
        Text lastTransactionText = new Text("Last Transaction:");
        Label lastTransactionLabel = new Label("Never");
        accountController.ch_last_transaction = lastTransactionLabel;

        //----- Monthly Fee -----
        Text monthlyFeeText = new Text("Monthly Fee:");
        Label monthlyFeeLabel = new Label("0.00");
        accountController.ch_monthly_fee = monthlyFeeLabel;

        //----- Salary Domiciliation -----
        Text salaryDomiciliationText = new Text("Salary Domiciliation:");
        Label salaryDomiciliationLabel = new Label("No");
        accountController.ch_salary_domiciliation = salaryDomiciliationLabel;

        //----- Account Status -----
        Text accountStatusText = new Text("Account Status:");
        Label accountStatusLabel = new Label("Active");
        accountController.ch_status = accountStatusLabel;

        //----- Add All Items To Checking Details Box -----
        checkingDetailsBox.getChildren().addAll(
                accountNumberText, accountNumberLabel,
                transactionLimitText, transactionLimitLabel,
                dateCreatedText, dateCreatedLabel,
                balanceText, balanceLabel,
                lastTransactionText, lastTransactionLabel,
                monthlyFeeText, monthlyFeeLabel,
                salaryDomiciliationText, salaryDomiciliationLabel,
                accountStatusText, accountStatusLabel
        );
        
        return checkingDetailsBox;
    }
    
    //----- Method To Create The Checking Account Transfer Box -----
    private VBox createCheckingTransferBox(AccountController accountController) {
        //----- Right Column - Transfer Functions -----
        VBox checkingTransferBox = new VBox(20);
        checkingTransferBox.setPrefWidth(300);

        //----- Move Funds To Savings -----
        Text moveToSavingsText = new Text("Move Funds To Savings Account:");
        moveToSavingsText.getStyleClass().add("title");

        TextField amountToSavingsField = new TextField();
        amountToSavingsField.setPrefHeight(40.0);
        amountToSavingsField.setPromptText("Enter amount");
        accountController.amount_to_sv = amountToSavingsField;

        Button transferToSavingsButton = new Button("Transfer");
        transferToSavingsButton.setMnemonicParsing(false);
        transferToSavingsButton.setPrefHeight(40.0);
        FontAwesomeIconView arrowDownIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_DOWN);
        arrowDownIcon.setSize("20");
        arrowDownIcon.setFill(Color.WHITE);
        transferToSavingsButton.setGraphic(arrowDownIcon);
        accountController.trans_to_sv_btn = transferToSavingsButton;

        Separator separator1 = new Separator();
        separator1.setPadding(new Insets(20, 0, 20, 0));

        //----- Account Management Section -----
        Text accountManagementText = new Text("Account Management:");
        accountManagementText.getStyleClass().add("title");

        VBox managementContainer = new VBox(10);
        managementContainer.getStyleClass().add("management_container");
        managementContainer.setPadding(new Insets(15, 15, 15, 15));

        Text savingsGoalText = new Text("Set Savings Goal:");
        TextField savingsGoalField = new TextField();
        savingsGoalField.setPromptText("Enter goal amount");
        accountController.sv_goal_amount = savingsGoalField;

        Button setGoalButton = new Button("Set Goal");
        setGoalButton.setMnemonicParsing(false);
        FontAwesomeIconView bullseyeIcon = new FontAwesomeIconView(FontAwesomeIcon.BULLSEYE);
        bullseyeIcon.setSize("16");
        bullseyeIcon.setFill(Color.WHITE);
        setGoalButton.setGraphic(bullseyeIcon);
        accountController.set_goal_btn = setGoalButton;

        managementContainer.getChildren().addAll(savingsGoalText, savingsGoalField, setGoalButton);

        //----- Add All Components To Checking Transfer Box -----
        checkingTransferBox.getChildren().addAll(
                moveToSavingsText, amountToSavingsField, transferToSavingsButton,
                separator1, accountManagementText, managementContainer
        );
        
        return checkingTransferBox;
    }
    
    //----- Method To Create The Savings Account Details Box -----
    private VBox createSavingsDetailsBox(AccountController accountController) {
        //----- Left Column - Account Details -----
        VBox savingsDetailsBox = new VBox(10);
        savingsDetailsBox.setPrefWidth(450.0);
        savingsDetailsBox.getStyleClass().add("account_box");
        savingsDetailsBox.setPadding(new Insets(20, 20, 20, 20));

        //----- Account Number -----
        Text svAccountNumberText = new Text("Account Number:");
        Label svAccountNumberLabel = new Label("3452 4950");
        accountController.sv_acc_num = svAccountNumberLabel;

        //----- Withdrawal Limit -----
        Text withdrawalLimitText = new Text("Withdrawal Limit:");
        Label withdrawalLimitLabel = new Label("2,000.00");
        accountController.withdrawal_limit = withdrawalLimitLabel;

        //----- Date Created -----
        Text svDateCreatedText = new Text("Date Created:");
        Label svDateCreatedLabel = new Label("2025-02-22");
        accountController.sv_acc_date = svDateCreatedLabel;

        //----- Balance -----   
        Text svBalanceText = new Text("Balance:");
        Label svBalanceLabel = new Label("13,000.00");
        accountController.sv_acc_bal = svBalanceLabel;

        //----- Interest Rate -----
        Text interestRateText = new Text("Interest Rate:");
        Label interestRateLabel = new Label("1.5%");
        accountController.sv_interest_rate = interestRateLabel;

        //----- Compounding Frequency -----
        Text compoundingText = new Text("Compounding Frequency:");
        Label compoundingLabel = new Label("Monthly");
        accountController.sv_compounding = compoundingLabel;

        //----- Savings Goal -----
        Text svGoalText = new Text("Savings Goal:");
        Label svGoalLabel = new Label("0.00");
        accountController.sv_goal = svGoalLabel;

        //----- Account Status -----
        Text svAccountStatusText = new Text("Account Status:");
        Label svAccountStatusLabel = new Label("Active");
        accountController.sv_status = svAccountStatusLabel;

        //----- Add All Items To Savings Details Box -----
        savingsDetailsBox.getChildren().addAll(
                svAccountNumberText, svAccountNumberLabel,
                withdrawalLimitText, withdrawalLimitLabel,
                svDateCreatedText, svDateCreatedLabel,
                svBalanceText, svBalanceLabel,
                interestRateText, interestRateLabel,
                compoundingText, compoundingLabel,
                svGoalText, svGoalLabel,
                svAccountStatusText, svAccountStatusLabel
        );
        
        return savingsDetailsBox;
    }
    
    //----- Method To Create The Savings Account Transfer Box -----
    private VBox createSavingsTransferBox(AccountController accountController) {
        //----- Right Column - Transfer Functions -----
        VBox savingsTransferBox = new VBox(20);
        savingsTransferBox.setPrefWidth(300);

        //----- Move Funds To Checking -----
        Text moveToCheckingText = new Text("Move Funds To Checkings Account:");
        moveToCheckingText.getStyleClass().add("title");

        TextField amountToCheckingField = new TextField();
        amountToCheckingField.setPrefHeight(40.0);
        amountToCheckingField.setPromptText("Enter amount");
        accountController.amount_to_ch = amountToCheckingField;

        Button transferToCheckingButton = new Button("Transfer");
        transferToCheckingButton.setMnemonicParsing(false);
        transferToCheckingButton.setPrefHeight(40.0);
        FontAwesomeIconView arrowUpIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_UP);
        arrowUpIcon.setSize("20");
        arrowUpIcon.setFill(Color.WHITE);
        transferToCheckingButton.setGraphic(arrowUpIcon);
        accountController.trans_to_ch_btn = transferToCheckingButton;

        Separator separator2 = new Separator();
        separator2.setPadding(new Insets(20, 0, 20, 0));

        //----- Interest Projection -----
        VBox projectionContainer = new VBox(10);
        projectionContainer.getStyleClass().add("management_container");
        projectionContainer.setPadding(new Insets(15, 15, 15, 15));

        Text projectionText = new Text("Interest Projection (months):");
        TextField projectMonthsField = new TextField();
        projectMonthsField.setPromptText("Enter months");
        accountController.project_months = projectMonthsField;

        Button calculateButton = new Button("Calculate");
        calculateButton.setMnemonicParsing(false);
        FontAwesomeIconView calculatorIcon = new FontAwesomeIconView(FontAwesomeIcon.CALCULATOR);
        calculatorIcon.setSize("16");
        calculatorIcon.setFill(Color.WHITE);
        calculateButton.setGraphic(calculatorIcon);
        accountController.project_btn = calculateButton;

        Text projectedBalanceText = new Text("Projected Balance:");
        Label projectedBalanceLabel = new Label("0.00");
        accountController.projected_balance = projectedBalanceLabel;

        projectionContainer.getChildren().addAll(
                projectionText, projectMonthsField, calculateButton,
                projectedBalanceText, projectedBalanceLabel
        );

        //----- Add All Components To Savings Transfer Box -----
        savingsTransferBox.getChildren().addAll(
                moveToCheckingText, amountToCheckingField, transferToCheckingButton,
                separator2, projectionContainer
        );
        
        return savingsTransferBox;
    }
} 
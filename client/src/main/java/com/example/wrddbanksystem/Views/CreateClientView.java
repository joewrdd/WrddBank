package com.example.wrddbanksystem.Views;

import com.example.wrddbanksystem.Controllers.Admin.CreateClientController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
 
//----- Create Client View -----
public class CreateClientView {
    
    //----- Create The Create Client View -----
    public AnchorPane createView() {
        try {
            //----- Create AnchorPane Container -----
            AnchorPane createClientView = new AnchorPane();
            createClientView.setPrefHeight(750);
            createClientView.setPrefWidth(800);
            createClientView.getStylesheets().add(getClass().getResource("/Styles/createclient.css").toExternalForm());
            createClientView.getStyleClass().add("create_client_container");

            //----- Create Scroll Pane For Content -----
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setPrefHeight(700);
            scrollPane.setPrefWidth(750);
            scrollPane.getStyleClass().add("scroll_pane");
            scrollPane.setFitToWidth(true);
            AnchorPane.setTopAnchor(scrollPane, 20.0);
            AnchorPane.setLeftAnchor(scrollPane, 20.0);
            AnchorPane.setRightAnchor(scrollPane, 20.0);
            AnchorPane.setBottomAnchor(scrollPane, 20.0);

            //----- Create The Content For Create Client View -----
            VBox contentBox = new VBox(20);
            contentBox.setPrefWidth(700);
            contentBox.setPadding(new Insets(30, 50, 30, 50));

            //----- Title Section -----
            Label titleLabel = new Label("Create New Client Account");
            titleLabel.getStyleClass().add("create_client_title");
            titleLabel.setFont(Font.font(26));

            //----- Personal Information Section -----
            Text personalInfoTitle = new Text("Personal Information");
            personalInfoTitle.getStyleClass().add("section_title");

            //----- Form Fields -----
            GridPane formGrid = new GridPane();
            formGrid.setHgap(20);
            formGrid.setVgap(20);
            formGrid.setPadding(new Insets(20, 0, 20, 0));

            //----- First Name Field -----
            Label firstNameLabel = new Label("First Name:");
            firstNameLabel.getStyleClass().add("field_label");
            TextField firstNameField = new TextField();
            firstNameField.setPromptText("Enter first name");
            firstNameField.setPrefWidth(300);
            firstNameField.setPrefHeight(35);
            formGrid.add(firstNameLabel, 0, 0);
            formGrid.add(firstNameField, 1, 0);

            //----- Last Name Field -----
            Label lastNameLabel = new Label("Last Name:");
            lastNameLabel.getStyleClass().add("field_label");
            TextField lastNameField = new TextField();
            lastNameField.setPromptText("Enter last name");
            lastNameField.setPrefWidth(300);
            lastNameField.setPrefHeight(35);
            formGrid.add(lastNameLabel, 0, 1);
            formGrid.add(lastNameField, 1, 1);

            //----- Password Field -----
            Label passwordLabel = new Label("Password:");
            passwordLabel.getStyleClass().add("field_label");
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Enter password");
            passwordField.setPrefWidth(300);
            passwordField.setPrefHeight(35);
            formGrid.add(passwordLabel, 0, 2);
            formGrid.add(passwordField, 1, 2);

            //----- Payee Address Section -----
            HBox payeeBox = new HBox(10);
            Label payeeAddressLabel = new Label("Payee Address:");
            payeeAddressLabel.getStyleClass().add("field_label");
            CheckBox payeeAddressBox = new CheckBox("Generate");
            payeeBox.getChildren().addAll(payeeAddressLabel, payeeAddressBox);
            formGrid.add(payeeBox, 0, 3);

            Label payeeAddressValue = new Label("");
            payeeAddressValue.getStyleClass().add("payee_address_value");
            formGrid.add(payeeAddressValue, 1, 3);

            //----- Checking Account Section -----
            Text checkingTitle = new Text("Checking Account");
            checkingTitle.getStyleClass().add("section_title");

            GridPane checkingGrid = new GridPane();
            checkingGrid.setHgap(20);
            checkingGrid.setVgap(20);
            checkingGrid.setPadding(new Insets(20, 0, 20, 0));

            //----- Checking Account Checkbox -----
            CheckBox checkingCheckbox = new CheckBox("Create Checking Account");
            checkingCheckbox.getStyleClass().add("account_checkbox");
            checkingGrid.add(checkingCheckbox, 0, 0, 2, 1);

            //----- Initial Amount Field -----
            Label checkingAmountLabel = new Label("Initial Amount ($):");
            checkingAmountLabel.getStyleClass().add("field_label");
            TextField checkingAmountField = new TextField();
            checkingAmountField.setPromptText("Enter initial amount");
            checkingAmountField.setPrefWidth(300);
            checkingAmountField.setPrefHeight(35);
            checkingGrid.add(checkingAmountLabel, 0, 1);
            checkingGrid.add(checkingAmountField, 1, 1);

            //----- Monthly Fee Field -----
            Label monthlyFeeLabel = new Label("Monthly Fee ($):");
            monthlyFeeLabel.getStyleClass().add("field_label");
            TextField monthlyFeeField = new TextField();
            monthlyFeeField.setText("0.00");
            monthlyFeeField.setPrefWidth(300);
            monthlyFeeField.setPrefHeight(35);
            checkingGrid.add(monthlyFeeLabel, 0, 2);
            checkingGrid.add(monthlyFeeField, 1, 2);

            //----- Salary Domiciliation Checkbox -----
            CheckBox salaryDomiciliationBox = new CheckBox("Has Salary Domiciliation");
            salaryDomiciliationBox.getStyleClass().add("account_checkbox");
            checkingGrid.add(salaryDomiciliationBox, 0, 3, 2, 1);

            //----- Savings Account Section -----
            Text savingsTitle = new Text("Savings Account");
            savingsTitle.getStyleClass().add("section_title");

            GridPane savingsGrid = new GridPane();
            savingsGrid.setHgap(20);
            savingsGrid.setVgap(20);
            savingsGrid.setPadding(new Insets(20, 0, 20, 0));

            //----- Savings Account Checkbox -----
            CheckBox savingsCheckbox = new CheckBox("Create Savings Account");
            savingsCheckbox.getStyleClass().add("account_checkbox");
            savingsGrid.add(savingsCheckbox, 0, 0, 2, 1);

            //----- Initial Amount Field -----
            Label savingsAmountLabel = new Label("Initial Amount ($):");
            savingsAmountLabel.getStyleClass().add("field_label");
            TextField savingsAmountField = new TextField();
            savingsAmountField.setPromptText("Enter initial amount");
            savingsAmountField.setPrefWidth(300);
            savingsAmountField.setPrefHeight(35);
            savingsGrid.add(savingsAmountLabel, 0, 1);
            savingsGrid.add(savingsAmountField, 1, 1);

            //----- Interest Rate Field -----
            Label interestRateLabel = new Label("Interest Rate (%):");
            interestRateLabel.getStyleClass().add("field_label");
            TextField interestRateField = new TextField();
            interestRateField.setText("1.50");
            interestRateField.setPrefWidth(300);
            interestRateField.setPrefHeight(35);
            savingsGrid.add(interestRateLabel, 0, 2);
            savingsGrid.add(interestRateField, 1, 2);

            //----- Compounding Frequency -----
            Label compoundingLabel = new Label("Compounding Frequency:");
            compoundingLabel.getStyleClass().add("field_label");
            ComboBox<String> compoundingFrequency = new ComboBox<>(
                FXCollections.observableArrayList("daily", "monthly", "annually")
            );
            compoundingFrequency.setValue("monthly");
            compoundingFrequency.setPrefWidth(300);
            compoundingFrequency.setPrefHeight(35);
            savingsGrid.add(compoundingLabel, 0, 3);
            savingsGrid.add(compoundingFrequency, 1, 3);

            //----- Goal Amount Field -----
            Label goalAmountLabel = new Label("Goal Amount ($):");
            goalAmountLabel.getStyleClass().add("field_label");
            TextField goalAmountField = new TextField();
            goalAmountField.setText("0.00");
            goalAmountField.setPrefWidth(300);
            goalAmountField.setPrefHeight(35);
            savingsGrid.add(goalAmountLabel, 0, 4);
            savingsGrid.add(goalAmountField, 1, 4);

            //----- Create Button -----
            Button createButton = new Button("Create New Account");
            createButton.getStyleClass().add("create_client_btn");
            createButton.setPrefHeight(40);
            createButton.setPrefWidth(250);

            //----- Error Message -----
            Label errorLabel = new Label("");
            errorLabel.getStyleClass().add("error_lbl");
            errorLabel.setVisible(false);

            //----- Add Components To The Content Box -----
            contentBox.getChildren().addAll(
                titleLabel, 
                personalInfoTitle, formGrid,
                checkingTitle, checkingGrid,
                savingsTitle, savingsGrid,
                createButton, errorLabel
            );
            
            //----- Set Content To Scroll Pane -----
            scrollPane.setContent(contentBox);
            
            //----- Add Scroll Pane To The Main View -----
            createClientView.getChildren().add(scrollPane);

            //----- Create Controller And Link To UI Components -----
            CreateClientController controller = new CreateClientController();
            controller.fName_fld = firstNameField;
            controller.lName_fld = lastNameField;
            controller.password_fld = passwordField;
            controller.pAddress_box = payeeAddressBox;
            controller.pAddress_lbl = payeeAddressValue;
            
            //----- Link Checking Account Fields -----
            controller.ch_acc_checkbox = checkingCheckbox;
            controller.ch_amount_fld = checkingAmountField;
            controller.ch_monthly_fee_fld = monthlyFeeField;
            controller.ch_salary_domiciliation = salaryDomiciliationBox;
            
            //----- Link Savings Account Fields -----
            controller.sv_acc_checkbox = savingsCheckbox;
            controller.sv_amount_fld = savingsAmountField;
            controller.sv_interest_rate_fld = interestRateField;
            controller.sv_compounding_frequency = compoundingFrequency;
            controller.sv_goal_amount_fld = goalAmountField;
            
            controller.create_client_btn = createButton;
            controller.error_lbl = errorLabel;

            //----- Initialize Controller -----
            controller.initialize(null, null);
            
            return createClientView;
            
        } catch (Exception e) {
            e.printStackTrace();
            AnchorPane errorView = new AnchorPane();
            Label errorLabel = new Label("Error loading create client view. Please contact support.");
            errorLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
            errorView.getChildren().add(errorLabel);
            AnchorPane.setTopAnchor(errorLabel, 10.0);
            AnchorPane.setLeftAnchor(errorLabel, 10.0);
            return errorView;
        }
    }
} 
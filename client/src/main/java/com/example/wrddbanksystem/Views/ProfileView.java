package com.example.wrddbanksystem.Views;

import com.example.wrddbanksystem.Controllers.Client.ProfileController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

//----- Creates And Manages The Profile View For The Client Interface -----
public class ProfileView {

    //----- Creates The Profile View With All Its Components -----
    public AnchorPane createView() {
        try {
            AnchorPane profileView = new AnchorPane();
            profileView.setPrefHeight(750.0);
            profileView.setPrefWidth(850.0);
            profileView.getStyleClass().add("profile_view_container");
            profileView.getStylesheets().add(getClass().getResource("/Styles/profile.css").toExternalForm());

            ProfileController profileController = new ProfileController();

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.getStyleClass().add("transparent-scroll-pane");
            AnchorPane.setBottomAnchor(scrollPane, 0.0);
            AnchorPane.setLeftAnchor(scrollPane, 0.0);
            AnchorPane.setRightAnchor(scrollPane, 0.0);
            AnchorPane.setTopAnchor(scrollPane, 0.0);

            VBox mainContainer = new VBox(30);
            mainContainer.setStyle("-fx-padding: 20 20 40 20;");

            //----- Create Profile Header Section -----
            HBox headerBox = createHeaderSection(profileController);

            //----- Create Personal Information Section -----
            Text personalInfoTitle = new Text("Personal Information");
            personalInfoTitle.getStyleClass().add("section-title");

            GridPane personalInfoGrid = createPersonalInfoSection(profileController);

            //----- Create Security Section -----
            Text securityTitle = new Text("Security");
            securityTitle.getStyleClass().add("section-title");

            GridPane securityGrid = createSecuritySection(profileController);

            //----- Create Account Management Section -----
            Text accountManagementTitle = new Text("Account Management");
            accountManagementTitle.getStyleClass().add("section-title");

            VBox accountManagementBox = createAccountManagementSection(profileController);

            //----- Create Status Label -----
            Label statusLabel = new Label();
            statusLabel.getStyleClass().add("status-label");
            profileController.status_lbl = statusLabel;

            //----- Add All Sections To Main Container -----
            mainContainer.getChildren().addAll(
                    headerBox,
                    personalInfoTitle, personalInfoGrid,
                    securityTitle, securityGrid,
                    accountManagementTitle, accountManagementBox,
                    statusLabel
            );

            //----- Set Main Container To Scroll Pane -----
            scrollPane.setContent(mainContainer);

            //----- Add Scroll Pane To Profile View -----
            profileView.getChildren().add(scrollPane);

            //----- Store Controller In UserData For Access Later (Needed For Refresh) -----
            profileView.setUserData(profileController);

            //----- Initialize Controller -----
            profileController.initialize(null, null);

            return profileView;

        } catch (Exception e) {
            e.printStackTrace();
            AnchorPane errorView = new AnchorPane();
            Label errorLabel = new Label("Error loading profile view. Please contact support.");
            errorLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
            errorView.getChildren().add(errorLabel);
            AnchorPane.setTopAnchor(errorLabel, 10.0);
            AnchorPane.setLeftAnchor(errorLabel, 10.0);
            return errorView;
        }
    }

    //----- Creates The Profile Header Section -----
    private HBox createHeaderSection(ProfileController profileController) {
        HBox headerBox = new HBox(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        FontAwesomeIconView userIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_CIRCLE);
        userIcon.setSize("60");
        userIcon.getStyleClass().add("profile-icon");

        VBox headerTextBox = new VBox();
        Text titleText = new Text("My Profile");
        titleText.getStyleClass().add("title");

        Text welcomeText = new Text("Welcome, John Doe");
        welcomeText.getStyleClass().add("welcome-text");
        profileController.welcome_text = welcomeText;

        headerTextBox.getChildren().addAll(titleText, welcomeText);
        headerBox.getChildren().addAll(userIcon, headerTextBox);

        return headerBox;
    }

    //----- Creates The Personal Information Section -----
    private GridPane createPersonalInfoSection(ProfileController profileController) {
        GridPane personalInfoGrid = new GridPane();
        personalInfoGrid.setHgap(30);
        personalInfoGrid.setVgap(15);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        col1.setMinWidth(100);
        col1.setPrefWidth(200);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);
        col2.setMinWidth(200);
        col2.setPrefWidth(300);

        personalInfoGrid.getColumnConstraints().addAll(col1, col2);

        //----- First Name Field -----
        Text firstNameText = new Text("First Name:");
        HBox firstNameBox = new HBox(10);
        firstNameBox.setAlignment(Pos.CENTER_LEFT);
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        firstNameField.getStyleClass().add("editable-field");
        profileController.firstName_fld = firstNameField;
        firstNameBox.getChildren().add(firstNameField);

        //----- Last Name Field -----
        Text lastNameText = new Text("Last Name:");
        HBox lastNameBox = new HBox(10);
        lastNameBox.setAlignment(Pos.CENTER_LEFT);
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        lastNameField.getStyleClass().add("editable-field");
        profileController.lastName_fld = lastNameField;
        lastNameBox.getChildren().add(lastNameField);

        //----- Payee Address Field (Read-Only) -----
        Text payeeAddressText = new Text("Payee Address:");
        TextField payeeAddressField = new TextField();
        payeeAddressField.setEditable(false);
        payeeAddressField.getStyleClass().add("readonly-field");
        profileController.payeeAddress_fld = payeeAddressField;

        //----- Date Created Field (Read-Only) -----
        Text dateCreatedText = new Text("Client Since:");
        TextField dateCreatedField = new TextField();
        dateCreatedField.setEditable(false);
        dateCreatedField.getStyleClass().add("readonly-field");
        profileController.dateCreated_fld = dateCreatedField;

        //----- Update Info Button -----
        Button updateInfoButton = new Button("Update Information");
        updateInfoButton.setMnemonicParsing(false);
        FontAwesomeIconView saveIcon = new FontAwesomeIconView(FontAwesomeIcon.SAVE);
        saveIcon.setSize("16");
        saveIcon.setFill(Color.WHITE);
        updateInfoButton.setGraphic(saveIcon);
        profileController.updateInfo_btn = updateInfoButton;

        //----- Add Elements To Personal Info Grid -----
        personalInfoGrid.add(firstNameText, 0, 0);
        personalInfoGrid.add(firstNameBox, 1, 0);
        personalInfoGrid.add(lastNameText, 0, 1);
        personalInfoGrid.add(lastNameBox, 1, 1);
        personalInfoGrid.add(payeeAddressText, 0, 2);
        personalInfoGrid.add(payeeAddressField, 1, 2);
        personalInfoGrid.add(dateCreatedText, 0, 3);
        personalInfoGrid.add(dateCreatedField, 1, 3);
        personalInfoGrid.add(updateInfoButton, 1, 4);

        return personalInfoGrid;
    }

    //----- Creates The Security Section -----
    private GridPane createSecuritySection(ProfileController profileController) {
        GridPane securityGrid = new GridPane();
        securityGrid.setHgap(30);
        securityGrid.setVgap(15);
        securityGrid.getStyleClass().add("security-section");

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        col1.setMinWidth(100);
        col1.setPrefWidth(200);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);
        col2.setMinWidth(200);
        col2.setPrefWidth(300);

        securityGrid.getColumnConstraints().addAll(col1, col2);

        //----- Current Password Field -----
        Text currentPasswordText = new Text("Current Password:");
        PasswordField currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Enter current password");
        profileController.currentPassword_fld = currentPasswordField;

        //----- New Password Field -----
        Text newPasswordText = new Text("New Password:");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password");
        profileController.newPassword_fld = newPasswordField;

        //----- Confirm Password Field -----
        Text confirmPasswordText = new Text("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");
        profileController.confirmPassword_fld = confirmPasswordField;

        //----- Change Password Button -----
        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setMnemonicParsing(false);
        FontAwesomeIconView lockIcon = new FontAwesomeIconView(FontAwesomeIcon.LOCK);
        lockIcon.setSize("16");
        lockIcon.setFill(Color.WHITE);
        changePasswordButton.setGraphic(lockIcon);
        profileController.changePassword_btn = changePasswordButton;

        //----- Add Elements To Security Grid -----
        securityGrid.add(currentPasswordText, 0, 0);
        securityGrid.add(currentPasswordField, 1, 0);
        securityGrid.add(newPasswordText, 0, 1);
        securityGrid.add(newPasswordField, 1, 1);
        securityGrid.add(confirmPasswordText, 0, 2);
        securityGrid.add(confirmPasswordField, 1, 2);
        securityGrid.add(changePasswordButton, 1, 3);

        return securityGrid;
    }

    //----- Creates The Account Management Section -----
    private VBox createAccountManagementSection(ProfileController profileController) {
        VBox accountManagementBox = new VBox(15);
        accountManagementBox.getStyleClass().add("account-management-section");

        HBox warningBox = new HBox(15);
        warningBox.setAlignment(Pos.CENTER_LEFT);
        FontAwesomeIconView warningIcon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
        warningIcon.setSize("24");
        warningIcon.getStyleClass().add("warning-icon");
        Text warningText = new Text("Deleting your account will permanently remove all your data.");
        warningText.getStyleClass().add("warning-text");
        warningBox.getChildren().addAll(warningIcon, warningText);

        Button deleteAccountButton = new Button("Delete Account");
        deleteAccountButton.setMnemonicParsing(false);
        deleteAccountButton.getStyleClass().add("delete-button");
        FontAwesomeIconView trashIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        trashIcon.setSize("16");
        trashIcon.setFill(Color.WHITE);
        deleteAccountButton.setGraphic(trashIcon);
        profileController.deleteAccount_btn = deleteAccountButton;

        accountManagementBox.getChildren().addAll(warningBox, deleteAccountButton);

        return accountManagementBox;
    }
} 
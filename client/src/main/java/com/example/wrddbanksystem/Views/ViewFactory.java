package com.example.wrddbanksystem.Views;

import com.example.wrddbanksystem.Controllers.Admin.AdminController;
import com.example.wrddbanksystem.Controllers.Admin.AdminMenuController;

import com.example.wrddbanksystem.Controllers.Client.ClientController;
import com.example.wrddbanksystem.Controllers.Client.ClientMenuController;

import com.example.wrddbanksystem.Controllers.LoginController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;   
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.control.PasswordField;


public class ViewFactory {
    private AccountType loginAccountType;

    //----- Client Views -----
    private final ObjectProperty<ClientMenuOptions> clientSelectedMenuItem;
    private AnchorPane dashboardView;
    private AnchorPane transactionsView;
    private AnchorPane accountsView;
    private AnchorPane profileView;

    //----- Admin Views -----               
    private final ObjectProperty<AdminMenuOptions> adminSelectedMenuItem;
    private AnchorPane adminDashboardView;
    private AnchorPane createClientView;
    private AnchorPane allClientsView;
    private AnchorPane depositsView;

    //----- Constructor -----
    public ViewFactory() {
        this.loginAccountType = AccountType.CLIENT;
        this.clientSelectedMenuItem = new SimpleObjectProperty<>();
        this.adminSelectedMenuItem = new SimpleObjectProperty<>();
    }

    //----- Method To Get The Login Account Type -----
    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    //----- Method To Set The Login Account Type -----
    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    /*
     * Client Views Section
     */

    //----- Method To Get The Selected Client Menu Item -----
    public ObjectProperty<ClientMenuOptions> getClientSelectedMenuItem() {
        return clientSelectedMenuItem;
    }

    //----- Method To Get The Dashboard View -----
    public AnchorPane getDashboardView() {
        if (dashboardView == null) {
            DashboardView dashboardViewCreator = new DashboardView();
            dashboardView = dashboardViewCreator.createView();
        }
        return dashboardView;
    }

    //----- Method To Clear Dashboard View -----
    public void clearDashboardView() {
        this.dashboardView = null;
    }

    //----- Method To Get The Transactions View -----
    public AnchorPane getTransactionsView() {
        if (transactionsView == null) {
            TransactionsView transactionsViewCreator = new TransactionsView();
            transactionsView = transactionsViewCreator.createView();
        }
        return transactionsView;
    }

    //----- Method To Get The Accounts View -----
    public AnchorPane getAccountsView() {
        if (accountsView == null) {
            AccountsView accountsViewCreator = new AccountsView();
            accountsView = accountsViewCreator.createView();
        }
        return accountsView;
    }

    //----- Method To Get The Profile View -----
    public AnchorPane getProfileView() {
        if (profileView == null) {
            ProfileView profileViewCreator = new ProfileView();
            profileView = profileViewCreator.createView();
        }
        return profileView;
    }

    //----- Method To Clear All Cached Views -----
    public void clearCachedViews() {
        this.dashboardView = null;
        this.transactionsView = null;
        this.accountsView = null;
        this.profileView = null;
        
        this.adminDashboardView = null;
        this.createClientView = null;
        this.allClientsView = null;
        this.depositsView = null;
    }

    //----- Method To Show The Client Window -----
    public void showClientWindow() {
        //----- Creates The Main BorderPane That Will Contain The Menu And Content -----
        BorderPane clientParent = new BorderPane();

        //----- Creates The Client Controller And Attaches It To The Parent -----
        ClientController clientController = new ClientController();
        clientController.client_parent = clientParent;

        //----- Creates The Client Menu (Left Side) -----
        VBox clientMenu = createClientMenu();
        clientParent.setLeft(clientMenu);

        //----- Sets The Initial Center Content To The Dashboard -----
        clientParent.setCenter(getDashboardView());

        //----- Creates The Scene With The Stylesheet -----
        Scene scene = new Scene(clientParent, 1000, 750);

        //----- Creates And Configures The Stage -----
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setTitle("Wrdd Bank - Client Dashboard");

        //----- Configures The Stage For Maximized Window With Standard Controls -----
        stage.setMaximized(true);
        stage.setResizable(true);

        //----- Initializes The Controller -----
        clientController.initialize(null, null);

        stage.show();
    }

    //----- Method To Create The Client Menu -----
    private VBox createClientMenu() {
        //----- Creates The Main VBox For The Menu -----
        VBox mainMenuContainer = new VBox();
        mainMenuContainer.setPrefHeight(750.0);
        mainMenuContainer.setPrefWidth(180.0);
        mainMenuContainer.getStyleClass().add("main_menu_container");
        mainMenuContainer.getStylesheets().add(getClass().getResource("/Styles/clientmenu.css").toExternalForm());

        //----- Creates The AnchorPane For The Content -----
        AnchorPane leftMenuContainer = new AnchorPane();
        leftMenuContainer.setPrefHeight(730.0);
        leftMenuContainer.setPrefWidth(160.0);
        leftMenuContainer.getStyleClass().add("left_menu_container");

        //----- Creates The Title Container -----
        VBox titleContainer = new VBox();
        titleContainer.setPrefHeight(80.0);
        titleContainer.setPrefWidth(160.0);
        titleContainer.getStyleClass().add("title_container");
        AnchorPane.setLeftAnchor(titleContainer, 0.0);
        AnchorPane.setRightAnchor(titleContainer, 0.0);
        AnchorPane.setTopAnchor(titleContainer, 0.0);

        //----- Adds The Bank Icon -----
        FontAwesomeIconView bankIcon = new FontAwesomeIconView(FontAwesomeIcon.BANK);
        bankIcon.setSize("30");

        //----- Adds The Bank Text -----
        Text bankText = new Text("Wrdd Bank");

        //----- Adds The Icon And Text To The Title Container -----
        titleContainer.getChildren().addAll(bankIcon, bankText);

        //----- Creates The Menu Container -----
        VBox menuContainer = new VBox();
        menuContainer.setPrefHeight(400.0);
        menuContainer.setPrefWidth(160.0);
        menuContainer.getStyleClass().add("menu_container");
        AnchorPane.setLeftAnchor(menuContainer, 0.0);
        AnchorPane.setRightAnchor(menuContainer, 0.0);
        AnchorPane.setTopAnchor(menuContainer, 100.0);

        //----- Creates The Menu Buttons -----
        Button dashboardBtn = new Button("Dashboard");
        FontAwesomeIconView homeIcon = new FontAwesomeIconView(FontAwesomeIcon.HOME);
        homeIcon.setSize("20");
        dashboardBtn.setGraphic(homeIcon);

        Button transactionBtn = new Button("Transaction");
        FontAwesomeIconView handshakeIcon = new FontAwesomeIconView(FontAwesomeIcon.HANDSHAKE_ALT);
        handshakeIcon.setSize("18");
        transactionBtn.setGraphic(handshakeIcon);

        Button accountsBtn = new Button("Accounts");
        FontAwesomeIconView idCardIcon = new FontAwesomeIconView(FontAwesomeIcon.ID_CARD_ALT);
        idCardIcon.setSize("18");
        accountsBtn.setGraphic(idCardIcon);

        //----- Creates The Separator Line -----
        Line separatorLine = new Line();
        separatorLine.setEndX(120.0);

        Button profileBtn = new Button("Profile");
        profileBtn.getStyleClass().add("alt_menu_btn");
        FontAwesomeIconView userIcon = new FontAwesomeIconView(FontAwesomeIcon.USER);
        userIcon.setSize("18");
        profileBtn.setGraphic(userIcon);

        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("alt_menu_btn");
        FontAwesomeIconView externalLinkIcon = new FontAwesomeIconView(FontAwesomeIcon.EXTERNAL_LINK);
        externalLinkIcon.setSize("18");
        logoutBtn.setGraphic(externalLinkIcon);

        //----- Adds The Buttons To The Menu Container -----
        menuContainer.getChildren().addAll(dashboardBtn, transactionBtn, accountsBtn,
                separatorLine, profileBtn, logoutBtn);

        //----- Creates The Report Bugs Container -----
        VBox reportBugsContainer = new VBox();
        reportBugsContainer.setPrefHeight(120.0);
        reportBugsContainer.setPrefWidth(160.0);
        reportBugsContainer.getStyleClass().add("report_bugs_container");
        AnchorPane.setBottomAnchor(reportBugsContainer, 14.0);
        AnchorPane.setLeftAnchor(reportBugsContainer, 14.0);
        AnchorPane.setRightAnchor(reportBugsContainer, 14.0);

        //----- Adds The Text To The Report Container -----
        Text reportText = new Text("Report Suggestion/Bug?");
        Label reportLabel = new Label("Use this to report any errors or suggestions");
        reportLabel.setPrefHeight(34.0);
        reportLabel.setPrefWidth(162.0);

        //----- Creates The Report Button -----
        Button reportBtn = new Button("Report");
        FontAwesomeIconView plusIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
        plusIcon.setSize("12");
        reportBtn.setGraphic(plusIcon);

        //----- Adds The Elements To The Report Bugs Container -----
        reportBugsContainer.getChildren().addAll(reportText, reportLabel, reportBtn);

        //----- Adds The Containers To The Left Menu -----
        leftMenuContainer.getChildren().addAll(titleContainer, menuContainer, reportBugsContainer);

        //----- Adds The Left Menu To The Main Container -----
        mainMenuContainer.getChildren().add(leftMenuContainer);

        //----- Creates The Client Menu Controller And Connects To The Buttons -----
        ClientMenuController menuController = new ClientMenuController();
        menuController.dashboard_btn = dashboardBtn;
        menuController.transaction_btn = transactionBtn;
        menuController.accounts_btn = accountsBtn;
        menuController.profile_btn = profileBtn;
        menuController.logout_btn = logoutBtn;
        menuController.report_btn = reportBtn;

        //----- Initializes The Controller -----
        menuController.initialize(null, null);

        return mainMenuContainer;
    }

    /*
     * Admin Views Section
     */

    //----- Method To Get The Selected Admin Menu Item -----
    public ObjectProperty<AdminMenuOptions> getAdminSelectedMenuItem() {
        return adminSelectedMenuItem;
    }

    //----- Method To Get The Admin Dashboard View -----
    public AnchorPane getAdminDashboardView() {
        if (adminDashboardView == null) {
            //----- Uses The AdminDashboardView Class To Create The View -----
            AdminDashboardView adminDashboardViewCreator = new AdminDashboardView();
            adminDashboardView = adminDashboardViewCreator.createView();
        }
        return adminDashboardView;
    }

    //----- Method To Get The Create Client View -----
    public AnchorPane getCreateClientView() {
        if (createClientView == null) {
            //----- Uses The CreateClientView Class To Create The View -----
            CreateClientView createClientViewCreator = new CreateClientView();
            createClientView = createClientViewCreator.createView();
        }
        return createClientView;
    }

    //----- Method To Get The All Clients View -----
    public AnchorPane getAllClientsView() {
        if (allClientsView == null) {
            //----- Uses The AllClientsView Class To Create The View -----
            AllClientsView allClientsViewCreator = new AllClientsView();
            allClientsView = allClientsViewCreator.createView();
        }
        return allClientsView;
    }

    //----- Method To Get The Deposits View -----
    public AnchorPane getDepositsView() {
        if (depositsView == null) {
            //----- Uses The DepositsView Class To Create The View -----
            DepositsView depositsViewCreator = new DepositsView();
            depositsView = depositsViewCreator.createView();
        }
        return depositsView;
    }

    //----- Method To Show The Admin Window -----
    public void showAdminWindow() {
        //----- Creates The Main BorderPane That Will Contain The Menu And Content -----
        BorderPane adminParent = new BorderPane();

        //----- Creates The Admin Controller And Attaches It To The Parent -----
        AdminController adminController = new AdminController();
        adminController.admin_parent = adminParent;

        //----- Creates The Admin Menu (Left Side) -----
        VBox adminMenu = createAdminMenu();
        adminParent.setLeft(adminMenu);

        //----- Sets The Initial Center Content To The Create Client View -----
        adminParent.setCenter(getCreateClientView());

        //----- Creates The Scene -----
        Scene scene = new Scene(adminParent, 900, 720);

        //----- Creates And Configures The Stage -----
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setTitle("Wrdd Bank - Admin Dashboard");

        //----- Configures The Stage For Maximized Window With Standard Controls -----
        stage.setMaximized(true);
        stage.setResizable(true);

        //----- Initializes The Controller -----
        adminController.initialize(null, null);

        stage.show();
    }

    //----- Method To Create The Admin Menu -----
    private VBox createAdminMenu() {
        //----- Creates The Main VBox For The Menu -----
        VBox mainMenuContainer = new VBox();
        mainMenuContainer.setPrefHeight(750.0);
        mainMenuContainer.setPrefWidth(180.0);
        mainMenuContainer.getStyleClass().add("main_menu_container");
        mainMenuContainer.getStylesheets().add(getClass().getResource("/Styles/adminmenu.css").toExternalForm());

        //----- Creates The AnchorPane For The Content -----
        AnchorPane leftMenuContainer = new AnchorPane();
        leftMenuContainer.setPrefHeight(730.0);
        leftMenuContainer.setPrefWidth(160.0);
        leftMenuContainer.getStyleClass().add("left_menu_container");

        //----- Creates The Title Container -----
        VBox titleContainer = new VBox();
        titleContainer.setPrefHeight(80.0);
        titleContainer.setPrefWidth(160.0);
        titleContainer.getStyleClass().add("title_container");
        AnchorPane.setLeftAnchor(titleContainer, 0.0);
        AnchorPane.setRightAnchor(titleContainer, 0.0);
        AnchorPane.setTopAnchor(titleContainer, 0.0);

        //----- Adds The Bank Icon -----
        FontAwesomeIconView bankIcon = new FontAwesomeIconView(FontAwesomeIcon.BANK);
        bankIcon.setSize("30");

        //----- Adds The Bank Text -----
        Text bankText = new Text("Wrdd Bank");

        //----- Adds The Icon And Text To The Title Container -----
        titleContainer.getChildren().addAll(bankIcon, bankText);

        //----- Creates The Menu Container -----
        VBox menuContainer = new VBox();
        menuContainer.setPrefHeight(400.0);
        menuContainer.setPrefWidth(160.0);
        menuContainer.getStyleClass().add("menu_container");
        AnchorPane.setLeftAnchor(menuContainer, 0.0);
        AnchorPane.setRightAnchor(menuContainer, 0.0);
        AnchorPane.setTopAnchor(menuContainer, 100.0);

        //----- Creates The Menu Buttons -----
        Button dashboardBtn = new Button("Dashboard");
        FontAwesomeIconView dashboardIcon = new FontAwesomeIconView(FontAwesomeIcon.DASHBOARD);
        dashboardIcon.setSize("18");
        dashboardBtn.setGraphic(dashboardIcon);

        Button createClientBtn = new Button("Create ");
        FontAwesomeIconView userPlusIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
        userPlusIcon.setSize("18");
        createClientBtn.setGraphic(userPlusIcon);

        Button clientsBtn = new Button("Clients");
        FontAwesomeIconView usersIcon = new FontAwesomeIconView(FontAwesomeIcon.USERS);
        usersIcon.setSize("18");
        clientsBtn.setGraphic(usersIcon);

        Button depositBtn = new Button("Deposit");
        FontAwesomeIconView creditCardIcon = new FontAwesomeIconView(FontAwesomeIcon.CREDIT_CARD);
        creditCardIcon.setSize("18");
        depositBtn.setGraphic(creditCardIcon);

        //----- Creates The Separator Line -----
        Line separatorLine = new Line();
        separatorLine.setEndX(120.0);

        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("alt_menu_btn");
        FontAwesomeIconView externalLinkIcon = new FontAwesomeIconView(FontAwesomeIcon.EXTERNAL_LINK);
        externalLinkIcon.setSize("20");
        logoutBtn.setGraphic(externalLinkIcon);

        //----- Adds The Buttons To The Menu Container -----
        menuContainer.getChildren().addAll(dashboardBtn, createClientBtn, clientsBtn,
                depositBtn, separatorLine, logoutBtn);

        //----- Adds The Containers To The Left Menu -----
        leftMenuContainer.getChildren().addAll(titleContainer, menuContainer);

        //----- Adds The Left Menu To The Main Container -----
        mainMenuContainer.getChildren().add(leftMenuContainer);

        //----- Creates The Admin Menu Controller And Connects To The Buttons -----
        AdminMenuController menuController = new AdminMenuController();
        menuController.dashboard_btn = dashboardBtn;
        menuController.create_client_btn = createClientBtn;
        menuController.clients_btn = clientsBtn;
        menuController.deposit_btn = depositBtn;
        menuController.logout_btn = logoutBtn;

        //----- Initializes The Controller -----
        menuController.initialize(null, null);

        return mainMenuContainer;
    }

    //----- Method To Show The Login Window -----
    public void showLoginWindow() {
        //----- Clears All Cached Views To Prevent Stale Data -----
        clearCachedViews();
        
        //----- Creates The Main Container -----
        AnchorPane root = new AnchorPane();
        root.setPrefHeight(400.0);
        root.setPrefWidth(600.0);
        root.getStyleClass().add("login_container");

        //----- Creates The Left Logo Container -----
        VBox logoContainer = new VBox();
        logoContainer.setPrefHeight(400.0);
        logoContainer.setPrefWidth(200.0);
        logoContainer.getStyleClass().add("login_logo_container");
        AnchorPane.setBottomAnchor(logoContainer, 0.0);
        AnchorPane.setLeftAnchor(logoContainer, 0.0);
        AnchorPane.setTopAnchor(logoContainer, 0.0);

        //----- Creates The Bank Icon -----
        FontAwesomeIconView bankIcon = new FontAwesomeIconView(FontAwesomeIcon.BANK);
        bankIcon.setSize("30");

        //----- Creates The Bank Text -----
        Text bankText = new Text("Wrdd Bank");

        //----- Adds The Icon And Text To The Logo Container -----
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.getChildren().addAll(bankIcon, bankText);

        //----- Creates The Right Form Container -----
        VBox formContainer = new VBox(15); 
        formContainer.setPrefHeight(350.0);
        formContainer.setPrefWidth(350.0);
        formContainer.getStyleClass().add("login_form_container");
        AnchorPane.setRightAnchor(formContainer, 20.0);
        AnchorPane.setTopAnchor(formContainer, 20.0);

        //----- Creates The Form Elements -----
        Label choicePromptLabel = new Label("Choose Your Account Type:");
        choicePromptLabel.setId("choice-prompt-text");

        ChoiceBox<AccountType> accountSelector = new ChoiceBox<>();
        accountSelector.setPrefWidth(90.0);
        accountSelector.getStyleClass().add("account_selector");

        Label payeeAddressLabel = new Label("Payee Address:");

        TextField payeeAddressField = new TextField();
        payeeAddressField.getStyleClass().add("input_field");

        Label passwordLabel = new Label("Password:");

        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().add("input_field");

        Button loginButton = new Button("Login");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error_message");
        errorLabel.setVisible(false);

        //----- Adds The Form Elements To The Form Container -----
        formContainer.getChildren().addAll(
                choicePromptLabel,
                accountSelector,
                payeeAddressLabel,
                payeeAddressField,
                passwordLabel,
                passwordField,
                loginButton,
                errorLabel
        );

        //----- Adds The Containers To The Root -----
        root.getChildren().addAll(logoContainer, formContainer);

        //----- Creates The Login Controller And Connects To The UI -----
        LoginController loginController = new LoginController();
        loginController.acc_selector = accountSelector;
        loginController.payee_address_lbl = payeeAddressLabel;
        loginController.payee_address_field = payeeAddressField;
        loginController.password_lbl = passwordLabel;
        loginController.password_field = passwordField;
        loginController.login_btn = loginButton;
        loginController.error_lbl = errorLabel;

        //----- Creates The Scene With The CSS -----
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/Styles/login.css").toExternalForm());

        //----- Creates And Shows The Stage -----
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setResizable(false);
        stage.setTitle("Wrdd Bank");

        //----- Initializes The Controller -----
        loginController.initialize(null, null);

        stage.show();
    }

    //----- Method To Show A Dialog Message -----
    public void showDialogMessage(String sender, String message) {
        try {
            //----- Gets The Current Active Stage -----
            Stage primaryStage = null;
            for (Stage stage : javafx.stage.Window.getWindows().filtered(window -> window instanceof Stage).stream().map(window -> (Stage) window).toList()) {
                if (stage.isShowing()) {
                    primaryStage = stage;
                    break;
                }
            }
            
            if (primaryStage == null) {
                //----- Fallback To Traditional Dialog If No Stage Is Found -----
                showTraditionalDialog(sender, message);
                return;
            }
            
            //----- Creates The Overlay Pane That Covers The Entire Application -----
            StackPane overlayPane = new StackPane();
            overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
            
            //----- Creates The Dialog Content -----
            VBox dialogBox = new VBox(10);
            dialogBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 5; -fx-max-width: 400px;");
            dialogBox.setMaxWidth(400);
            dialogBox.setMaxHeight(200);
            dialogBox.setAlignment(Pos.CENTER);
            
            //----- Creates The Dialog Header -----
            HBox headerBox = new HBox(10);
            headerBox.setAlignment(Pos.CENTER);
            
            FontAwesomeIconView infoIcon = new FontAwesomeIconView(FontAwesomeIcon.INFO_CIRCLE);
            infoIcon.setFill(Color.valueOf("#3498db"));
            infoIcon.setSize("30");
            
            Label titleLabel = new Label(sender);
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            
            headerBox.getChildren().addAll(infoIcon, titleLabel);
            
            //----- Creates The Message Label -----
            Label messageLabel = new Label(message);
            messageLabel.setStyle("-fx-font-size: 14px;");
            messageLabel.setWrapText(true);
            
            //----- Creates The OK Button -----
            Button okButton = new Button("OK");
            okButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-min-width: 80px;");
            
            //----- Adds The Components To The Dialog Box -----
            dialogBox.getChildren().addAll(headerBox, messageLabel, okButton);
            
            //----- Adds The Dialog To The Overlay -----
            overlayPane.getChildren().add(dialogBox);
            
            //----- Gets The Root Pane Of The Application -----
            Scene scene = primaryStage.getScene();
            Parent root = scene.getRoot();
            
            if (root instanceof BorderPane) {
                BorderPane borderPane = (BorderPane) root;
                
                //----- Adds The Overlay To The Current Layout -----
                StackPane contentPane = new StackPane();
                Node originalCenter = borderPane.getCenter();
                contentPane.getChildren().addAll(originalCenter, overlayPane);
                
                borderPane.setCenter(contentPane);
                
                //----- Removes The Overlay When The OK Button Is Clicked -----
                okButton.setOnAction(e -> {
                    borderPane.setCenter(originalCenter);
                });
            } else {
                //----- If Not BorderPane, Fall Back To Traditional Dialog -----
                showTraditionalDialog(sender, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //----- Fallback To Traditional Dialog In Case Of Errors -----
            showTraditionalDialog(sender, message);
        }
    }
    
    //----- Fallback Method For Traditional Dialog -----
    private void showTraditionalDialog(String sender, String message) {
        StackPane stackPane = new StackPane();
        HBox hBox = new HBox(5);
        hBox.setAlignment(Pos.CENTER);
        Label senderLabel = new Label(sender);
        Label messageLabel = new Label(message);
        hBox.getChildren().addAll(senderLabel, messageLabel);
        stackPane.getChildren().add(hBox);
        Scene scene = new Scene(stackPane, 300, 100);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL); // Cannot Be Dismissible
        stage.setTitle("Message");
        stage.setScene(scene);
        stage.show();
    }

    //----- Method To Create A Stage -----
    private void createStage(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setResizable(false);
        stage.setTitle("Wrdd Bank");
        stage.show();
    }

    //----- Method To Close A Stage -----   
    public void closeStage(Stage stage) {
        try {
            if (stage != null && stage.isShowing()) {
                //----- Hides The Stage Instead Of Closing It To Maintain The Application Lifecycle -----
                stage.hide();
            }
        } catch (Exception e) {
            System.err.println("Error closing stage: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

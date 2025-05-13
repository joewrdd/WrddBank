package com.example.wrddbanksystem.Views;

import com.example.wrddbanksystem.Controllers.Admin.AllClientsController;
import com.example.wrddbanksystem.Models.CheckingAccounts;
import com.example.wrddbanksystem.Models.Client;
import com.example.wrddbanksystem.Core.Model;
import com.example.wrddbanksystem.Models.SavingsAccounts;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.List;

//----- Class To Create And Manage The All Clients View For The Admin Interface -----
public class AllClientsView {

    //----- Method To Create The All Clients View -----
    public AnchorPane createView() {
        try {
            //----- Create StackPane To Hold Both The Client List And Client Details Views -----
            StackPane rootPane = new StackPane();
            rootPane.setPrefHeight(750);
            rootPane.setPrefWidth(850);

            //----- Create AnchorPane For Clients List -----
            AnchorPane clientsListPane = createClientsListPane();

            //----- Add Panes To Root Stack Pane -----
            rootPane.getChildren().add(clientsListPane);

            //----- Wrap In An AnchorPane For Consistent API -----
            AnchorPane allClientsView = new AnchorPane();
            allClientsView.getStylesheets().add(getClass().getResource("/Styles/allclients.css").toExternalForm());
            AnchorPane.setTopAnchor(rootPane, 0.0);
            AnchorPane.setRightAnchor(rootPane, 0.0);
            AnchorPane.setBottomAnchor(rootPane, 0.0);
            AnchorPane.setLeftAnchor(rootPane, 0.0);
            allClientsView.getChildren().add(rootPane);

            return allClientsView;

        } catch (Exception e) {
            e.printStackTrace();
            AnchorPane errorView = new AnchorPane();
            Label errorLabel = new Label("Error loading all clients view. Please contact support.");
            errorLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
            errorView.getChildren().add(errorLabel);
            AnchorPane.setTopAnchor(errorLabel, 10.0);
            AnchorPane.setLeftAnchor(errorLabel, 10.0);
            return errorView;
        }
    }

    //----- Method To Create The Clients List Pane -----
    private AnchorPane createClientsListPane() {
        AnchorPane clientsListPane = new AnchorPane();
        clientsListPane.getStyleClass().add("clients_list_pane");

        //----- Title Label -----
        Label titleLabel = new Label("All Clients");
        titleLabel.getStyleClass().add("clients_title");
        titleLabel.setLayoutX(50);
        titleLabel.setLayoutY(30);

        //----- Create Search Section With HBox That Includes Both Search And Refresh -----
        HBox controlsBox = new HBox(10);
        controlsBox.setLayoutX(50);
        controlsBox.setLayoutY(70);
        controlsBox.setPrefWidth(700);

        //----- Search Field And Button On The Left -----
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchBox, Priority.ALWAYS);

        TextField searchField = new TextField();
        searchField.setPromptText("Search clients by name or payee address");
        searchField.setPrefWidth(400);
        searchField.setPrefHeight(35);
        searchField.getStyleClass().add("search_field");

        Button searchButton = new Button("Search");
        searchButton.setPrefHeight(35);
        searchButton.getStyleClass().add("search_button");

        FontAwesomeIconView searchIcon = new FontAwesomeIconView(FontAwesomeIcon.SEARCH);
        searchIcon.setFill(Color.WHITE);
        searchButton.setGraphic(searchIcon);

        searchBox.getChildren().addAll(searchField, searchButton);

        //----- Refresh Button On The Right -----
        HBox refreshBox = new HBox();
        refreshBox.setAlignment(Pos.CENTER_RIGHT);

        Button refreshButton = new Button("Refresh List");
        refreshButton.getStyleClass().add("refresh_button");
        refreshButton.setPrefWidth(120);
        refreshButton.setPrefHeight(35);

        FontAwesomeIconView refreshIcon = new FontAwesomeIconView(FontAwesomeIcon.REFRESH);
        refreshIcon.setFill(Color.WHITE);
        refreshButton.setGraphic(refreshIcon);

        refreshBox.getChildren().add(refreshButton);

        //----- Add Both Search And Refresh To The Main Controls Box -----
        controlsBox.getChildren().addAll(searchBox, refreshBox);

        //----- ListView For Clients -----
        ListView<Client> clientsListView = new ListView<>();
        clientsListView.setPrefHeight(580);
        clientsListView.setPrefWidth(700);
        clientsListView.setLayoutX(50);
        clientsListView.setLayoutY(120);
        clientsListView.getStyleClass().add("clients_list");

        //----- Set Cell Factory -----
        clientsListView.setCellFactory(lv -> new ClientCellFactory());

        //----- Add Components To View -----
        clientsListPane.getChildren().addAll(titleLabel, controlsBox, clientsListView);

        //----- Create Controller And Link UI Components -----
        AllClientsController controller = new AllClientsController();
        controller.all_clients_listview = clientsListView;

        //----- Always Refresh Clients List From Database When View Is Loaded -----
        Model.getInstance().setClients();
        clientsListView.setItems(Model.getInstance().getClients());

        //----- Set Refresh Button Action -----
        refreshButton.setOnAction(e -> {
            //----- Clear Selection First To Avoid Potential Issues -----
            clientsListView.getSelectionModel().clearSelection();

            //----- Refresh Data From Database -----
            Model.getInstance().setClients();

            //----- Update List View With Fresh Data -----
            clientsListView.setItems(Model.getInstance().getClients());
            clientsListView.refresh();
        });

        //----- Connect Search Functionality -----
        searchButton.setOnAction(e -> {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                //----- Clear Selection First -----
                clientsListView.getSelectionModel().clearSelection();

                //----- Search For Clients With The Provided Term -----
                clientsListView.setItems(Model.getInstance().searchClient(searchTerm));
                clientsListView.refresh();
            } else {
                //----- If Search Is Empty, Show All Clients -----
                Model.getInstance().setClients();
                clientsListView.setItems(Model.getInstance().getClients());
                clientsListView.refresh();
            }
        });

        //----- Allow Searching By Pressing Enter In The Search Field -----
        searchField.setOnAction(e -> searchButton.fire());

        //----- Handle Item Click To Show Client Details -----
        clientsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                //----- Create And Display The Client Details Pane -----
                AnchorPane clientDetailsPane = createClientDetailsPane(newValue, clientsListPane.getScene().getRoot());

                //----- Get The Parent StackPane And Add The Details Pane -----
                StackPane parentPane = (StackPane) clientsListPane.getParent();
                if (!parentPane.getChildren().contains(clientDetailsPane)) {
                    parentPane.getChildren().add(clientDetailsPane);
                }
            }
        });

        //----- Initialize Controller -----
        controller.initialize(null, null);

        return clientsListPane;
    }

    //----- Method To Create The Client Details Pane -----
    private AnchorPane createClientDetailsPane(Client client, javafx.scene.Parent parentPane) {
        //----- Refresh Client Account Data -----
        try {
            //----- First Refresh Checking Account -----
            if (client.getPayeeAddress() != null) {
                try {
                    List<CheckingAccounts> checkingAccounts = Model.getInstance().getAccountService().getCheckingAccounts(client.getPayeeAddress());
                    if (checkingAccounts != null && !checkingAccounts.isEmpty()) {
                        client.checkingAccountProperty().set(checkingAccounts.get(0));
                    }
                } catch (Exception e) {
                    System.err.println("Error loading checking account for client details: " + e.getMessage());
                }

                //----- Then Refresh Savings Account -----
                try {
                    List<SavingsAccounts> savingsAccounts = Model.getInstance().getAccountService().getSavingsAccounts(client.getPayeeAddress());
                    if (savingsAccounts != null && !savingsAccounts.isEmpty()) {
                        client.savingsAccountProperty().set(savingsAccounts.get(0));
                    }
                } catch (Exception e) {
                    System.err.println("Error loading savings account for client details: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error refreshing client accounts: " + e.getMessage());
        }

        AnchorPane clientDetailsPane = new AnchorPane();
        clientDetailsPane.getStyleClass().add("client_details_pane");
        clientDetailsPane.setPrefHeight(750);
        clientDetailsPane.setPrefWidth(850);
        clientDetailsPane.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);");

        //----- Create Controller -----
        ClientDetailsController controller = new ClientDetailsController(client);

        //----- Header Section With Back Button -----
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(20));
        AnchorPane.setTopAnchor(headerBox, 0.0);
        AnchorPane.setLeftAnchor(headerBox, 0.0);
        AnchorPane.setRightAnchor(headerBox, 0.0);

        Button backButton = new Button();
        backButton.getStyleClass().add("back_button");
        FontAwesomeIconView backIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_LEFT);
        backIcon.setSize("20");
        backButton.setGraphic(backIcon);

        //----- Back Button Action -----
        backButton.setOnAction(e -> {
            StackPane parentStackPane = (StackPane) clientDetailsPane.getParent();
            parentStackPane.getChildren().remove(clientDetailsPane);
        });

        Label clientNameLabel = new Label(client.firstNameProperty().get() + " " + client.lastNameProperty().get());
        clientNameLabel.getStyleClass().add("client_name_label");

        headerBox.getChildren().addAll(backButton, clientNameLabel);

        //----- Create Client Info Section -----
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("details_scroll_pane");
        AnchorPane.setTopAnchor(scrollPane, 70.0);
        AnchorPane.setRightAnchor(scrollPane, 20.0);
        AnchorPane.setBottomAnchor(scrollPane, 20.0);
        AnchorPane.setLeftAnchor(scrollPane, 20.0);

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20));

        //----- Personal Information Section -----
        Label personalInfoLabel = new Label("Personal Information");
        personalInfoLabel.getStyleClass().add("section_label");

        GridPane personalInfoGrid = new GridPane();
        personalInfoGrid.setHgap(20);
        personalInfoGrid.setVgap(10);
        personalInfoGrid.getStyleClass().add("info_grid");

        //----- Client Fields -----
        Text firstNameLabel = new Text("First Name:");
        Label firstNameValue = new Label(client.firstNameProperty().get());
        controller.firstName_lbl = firstNameValue;

        Text lastNameLabel = new Text("Last Name:");
        Label lastNameValue = new Label(client.lastNameProperty().get());
        controller.lastName_lbl = lastNameValue;

        Text payeeAddressLabel = new Text("Payee Address:");
        Label payeeAddressValue = new Label(client.payeeAddressProperty().get());
        controller.payeeAddress_lbl = payeeAddressValue;

        Text dateCreatedLabel = new Text("Client Since:");
        Label dateCreatedValue = new Label(client.dateCreatedProperty().get().toString());
        controller.dateCreated_lbl = dateCreatedValue;

        //----- Add To Grid -----
        personalInfoGrid.add(firstNameLabel, 0, 0);
        personalInfoGrid.add(firstNameValue, 1, 0);
        personalInfoGrid.add(lastNameLabel, 0, 1);
        personalInfoGrid.add(lastNameValue, 1, 1);
        personalInfoGrid.add(payeeAddressLabel, 0, 2);
        personalInfoGrid.add(payeeAddressValue, 1, 2);
        personalInfoGrid.add(dateCreatedLabel, 0, 3);
        personalInfoGrid.add(dateCreatedValue, 1, 3);

        //----- Account Information Section -----
        Label accountInfoLabel = new Label("Account Information");
        accountInfoLabel.getStyleClass().add("section_label");

        //----- Checking Account -----
        TitledPane checkingAccountPane = new TitledPane();
        checkingAccountPane.setText("Checking Account");
        checkingAccountPane.setExpanded(true);

        GridPane checkingGrid = new GridPane();
        checkingGrid.setHgap(20);
        checkingGrid.setVgap(10);
        checkingGrid.getStyleClass().add("info_grid");
        checkingGrid.setPadding(new Insets(10));

        Text checkingNumLabel = new Text("Account Number:");
        Label checkingNumValue = new Label(client.checkingAccountProperty().get() != null ?
                client.checkingAccountProperty().get().accountNumberProperty().get() : "N/A");
        controller.checkingNum_lbl = checkingNumValue;

        Text checkingBalanceLabel = new Text("Balance:");
        Label checkingBalanceValue = new Label(client.checkingAccountProperty().get() != null ?
                "$" + client.checkingAccountProperty().get().balanceProperty().get() : "$0.00");
        controller.checkingBalance_lbl = checkingBalanceValue;

        Text checkingStatusLabel = new Text("Status:");
        Label checkingStatusValue = new Label(client.checkingAccountProperty().get() != null ?
                client.checkingAccountProperty().get().getStatus().toString() : "N/A");
        controller.checkingStatus_lbl = checkingStatusValue;

        //----- Add To Grid -----
        checkingGrid.add(checkingNumLabel, 0, 0);
        checkingGrid.add(checkingNumValue, 1, 0);
        checkingGrid.add(checkingBalanceLabel, 0, 1);
        checkingGrid.add(checkingBalanceValue, 1, 1);
        checkingGrid.add(checkingStatusLabel, 0, 2);
        checkingGrid.add(checkingStatusValue, 1, 2);

        checkingAccountPane.setContent(checkingGrid);

        //----- Savings Account -----
        TitledPane savingsAccountPane = new TitledPane();
        savingsAccountPane.setText("Savings Account");
        savingsAccountPane.setExpanded(true);

        GridPane savingsGrid = new GridPane();
        savingsGrid.setHgap(20);
        savingsGrid.setVgap(10);
        savingsGrid.getStyleClass().add("info_grid");
        savingsGrid.setPadding(new Insets(10));

        Text savingsNumLabel = new Text("Account Number:");
        Label savingsNumValue = new Label(client.savingsAccountProperty().get() != null ?
                client.savingsAccountProperty().get().accountNumberProperty().get() : "N/A");
        controller.savingsNum_lbl = savingsNumValue;

        Text savingsBalanceLabel = new Text("Balance:");
        Label savingsBalanceValue = new Label(client.savingsAccountProperty().get() != null ?
                "$" + client.savingsAccountProperty().get().balanceProperty().get() : "$0.00");
        controller.savingsBalance_lbl = savingsBalanceValue;

        Text savingsStatusLabel = new Text("Status:");
        Label savingsStatusValue = new Label(client.savingsAccountProperty().get() != null ?
                client.savingsAccountProperty().get().getStatus().toString() : "N/A");
        controller.savingsStatus_lbl = savingsStatusValue;

        //----- Add To Grid -----
        savingsGrid.add(savingsNumLabel, 0, 0);
        savingsGrid.add(savingsNumValue, 1, 0);
        savingsGrid.add(savingsBalanceLabel, 0, 1);
        savingsGrid.add(savingsBalanceValue, 1, 1);
        savingsGrid.add(savingsStatusLabel, 0, 2);
        savingsGrid.add(savingsStatusValue, 1, 2);

        savingsAccountPane.setContent(savingsGrid);

        //----- Account Actions Section -----
        Label accountActionsLabel = new Label("Account Actions");
        accountActionsLabel.getStyleClass().add("section_label");

        //----- Delete Client Button -----
        Button deleteClientButton = new Button("Delete Client");
        deleteClientButton.getStyleClass().add("delete_client_btn");
        FontAwesomeIconView trashIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        trashIcon.setFill(Color.WHITE);
        deleteClientButton.setGraphic(trashIcon);
        controller.delete_client_btn = deleteClientButton;

        //----- Status Message -----
        Label statusLabel = new Label();
        statusLabel.getStyleClass().add("status_label");
        statusLabel.setVisible(false);
        controller.status_lbl = statusLabel;

        //----- Add All Sections To The Content Box -----
        contentBox.getChildren().addAll(
                personalInfoLabel, personalInfoGrid,
                accountInfoLabel, checkingAccountPane, savingsAccountPane,
                accountActionsLabel, deleteClientButton, statusLabel
        );

        scrollPane.setContent(contentBox);

        //----- Add All Sections To The Details Pane -----
        clientDetailsPane.getChildren().addAll(headerBox, scrollPane);

        //----- Initialize Controller -----
        controller.initialize();

        return clientDetailsPane;
    }

    //----- Controller For Handling Client Details Operations -----
    private class ClientDetailsController {
        //----- UI Components -----
        public Label firstName_lbl;
        public Label lastName_lbl;
        public Label payeeAddress_lbl;
        public Label dateCreated_lbl;
        public Label checkingNum_lbl;
        public Label checkingBalance_lbl;
        public Label checkingStatus_lbl;
        public Label savingsNum_lbl;
        public Label savingsBalance_lbl;
        public Label savingsStatus_lbl;
        public Button delete_client_btn;
        public Label status_lbl;

        private final Client client;

        public ClientDetailsController(Client client) {
            this.client = client;
        }

        //----- Initializes The Controller With Client Data -----
        public void initialize() {
            //----- Set Up Delete Button Action -----
            delete_client_btn.setOnAction(e -> deleteClient());
        }

        //----- Handles The Deletion Of A Client -----
        private void deleteClient() {
            try {
                //----- Show Confirmation Dialog -----
                showDeleteConfirmationDialog(confirmed -> {
                    if (confirmed) {
                        //----- Delete Client Using The Service Layer -----
                        boolean success = Model.getInstance().deleteClient(client.payeeAddressProperty().get());

                        if (success) {
                            //----- Update Status Label And Refresh Client List -----
                            status_lbl.setText("Client deleted successfully");
                            status_lbl.setStyle("-fx-text-fill: green;");

                            //----- Find Parent Stack Pane To Close The Details View -----
                            StackPane parentStackPane = (StackPane) ((Node) status_lbl).getScene().getRoot();
                            if (parentStackPane instanceof StackPane) {
                                //----- Remove This Details Pane -----
                                for (Node node : parentStackPane.getChildren()) {
                                    if (node instanceof AnchorPane && node != parentStackPane.getChildren().get(0)) {
                                        parentStackPane.getChildren().remove(node);
                                        break;
                                    }
                                }
                            }

                            //----- Refresh The Clients List -----
                            Model.getInstance().setClients();
                        } else {
                            //----- Update Status Label With Error -----
                            status_lbl.setText("Failed to delete client");
                            status_lbl.setStyle("-fx-text-fill: red;");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                status_lbl.setText("Error: " + e.getMessage());
                status_lbl.setStyle("-fx-text-fill: red;");
            }
        }

        private void showDeleteConfirmationDialog(java.util.function.Consumer<Boolean> resultCallback) {
            //----- Create Confirmation Dialog As An Overlay -----
            BorderPane parentBorderPane = null;

            //----- Find The BorderPane Parent -----
            Parent parent = delete_client_btn.getScene().getRoot();
            if (parent instanceof StackPane && ((StackPane) parent).getChildren().size() > 0) {
                Node firstChild = ((StackPane) parent).getChildren().get(0);
                if (firstChild instanceof BorderPane) {
                    parentBorderPane = (BorderPane) firstChild;
                }
            }

            if (parentBorderPane != null) {
                //----- Create Overlay With Confirmation Dialog -----
                BorderPane finalParentBorderPane = parentBorderPane;
                Node originalCenter = finalParentBorderPane.getCenter();

                //----- Create Overlay Pane -----
                StackPane overlayPane = new StackPane();
                overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

                //----- Create Dialog Content -----
                VBox dialogBox = new VBox(15);
                dialogBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 5; -fx-max-width: 400px;");
                dialogBox.setMaxWidth(400);
                dialogBox.setMaxHeight(250);
                dialogBox.setAlignment(Pos.CENTER);

                //----- Title Label -----
                Label titleLabel = new Label("Delete Client");
                titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                //----- Warning Label -----
                Label warningLabel = new Label("Are you sure you want to delete this client?");
                warningLabel.setStyle("-fx-font-size: 14px;");
                warningLabel.setWrapText(true);

                Label infoLabel = new Label("This action cannot be undone. All client data will be permanently removed.");
                infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #e74c3c;");
                infoLabel.setWrapText(true);

                //----- Button Container -----
                HBox buttonBox = new HBox(20);
                buttonBox.setAlignment(Pos.CENTER);

                Button deleteButton = new Button("Delete");
                deleteButton.setPrefWidth(100);
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                Button cancelButton = new Button("Cancel");
                cancelButton.setPrefWidth(100);
                cancelButton.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");

                buttonBox.getChildren().addAll(cancelButton, deleteButton);

                //----- Add All Components To The Dialog -----
                dialogBox.getChildren().addAll(titleLabel, warningLabel, infoLabel, buttonBox);

                //----- Add Dialog To Overlay -----
                overlayPane.getChildren().add(dialogBox);

                //----- Create Content Pane -----
                StackPane contentPane = new StackPane();
                contentPane.getChildren().addAll(originalCenter, overlayPane);

                //----- Show Dialog -----
                finalParentBorderPane.setCenter(contentPane);

                //----- Set Button Actions -----
                deleteButton.setOnAction(e -> {
                    //----- Restore Original Center Content -----
                    finalParentBorderPane.setCenter(originalCenter);
                    //----- Return Result -----
                    resultCallback.accept(true);
                });

                cancelButton.setOnAction(e -> {
                    //----- Just Restore Original Center Content -----
                    finalParentBorderPane.setCenter(originalCenter);
                    //----- Return Result -----
                    resultCallback.accept(false);
                });
            } else {
                //----- Fall Back To Standard Alert If We Can't Find The BorderPane -----
                showTraditionalDeleteConfirmation(resultCallback);
            }
        }

        private void showTraditionalDeleteConfirmation(java.util.function.Consumer<Boolean> resultCallback) {
            //----- Confirm Deletion With Alert Dialog -----
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Client");
            alert.setHeaderText("Are you sure you want to delete this client?");
            alert.setContentText("This action cannot be undone. All client data will be permanently removed.");

            ButtonType confirmButton = new ButtonType("Delete", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(confirmButton, cancelButton);

            alert.showAndWait().ifPresent(buttonType -> {
                resultCallback.accept(buttonType == confirmButton);
            });
        }
    }
} 
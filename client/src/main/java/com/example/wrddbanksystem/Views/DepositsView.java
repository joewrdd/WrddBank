package com.example.wrddbanksystem.Views;

import com.example.wrddbanksystem.Controllers.Admin.DepositController;
import com.example.wrddbanksystem.Models.Client;
import com.example.wrddbanksystem.Core.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.CountDownLatch;

//----- Creates And Manages The Deposits View For The Admin Interface -----
public class DepositsView {

    //----- Create The Deposits View -----
    public AnchorPane createView() {
        try {
            AnchorPane depositsView = new AnchorPane();
            depositsView.setPrefHeight(750);
            depositsView.setPrefWidth(800);
            depositsView.getStylesheets().add(getClass().getResource("/Styles/deposit.css").toExternalForm());

            //----- Title Label -----
            Label titleLabel = new Label("Client Deposits");
            titleLabel.getStyleClass().add("deposit_title");
            titleLabel.setLayoutX(50);
            titleLabel.setLayoutY(30);

            //----- Form Fields -----
            GridPane formGrid = new GridPane();
            formGrid.setLayoutX(50);
            formGrid.setLayoutY(100);
            formGrid.setHgap(20);
            formGrid.setVgap(20);

            //----- Payee Address Field -----
            Label payeeAddressLabel = new Label("Client Address:");
            payeeAddressLabel.getStyleClass().add("field_label");
            TextField payeeAddressField = new TextField();
            payeeAddressField.setPromptText("Enter client payee address");
            payeeAddressField.setPrefWidth(300);
            payeeAddressField.setPrefHeight(35);
            formGrid.add(payeeAddressLabel, 0, 0);
            formGrid.add(payeeAddressField, 1, 0);

            //----- Search Button -----
            Button searchButton = new Button("Search");
            searchButton.getStyleClass().add("search_btn");
            searchButton.setPrefHeight(35);
            FontAwesomeIconView searchIcon = new FontAwesomeIconView(FontAwesomeIcon.SEARCH);
            searchIcon.setFill(Color.WHITE);
            searchButton.setGraphic(searchIcon);
            formGrid.add(searchButton, 2, 0);

            //----- Results ListView -----
            ListView<Client> resultsListView = new ListView<>();
            resultsListView.setPrefHeight(200);
            resultsListView.setPrefWidth(700);
            resultsListView.getStyleClass().add("results_list");
            formGrid.add(resultsListView, 0, 1, 3, 1);

            //----- Deposit Amount Field -----
            Label depositAmountLabel = new Label("Deposit Amount:");
            depositAmountLabel.getStyleClass().add("field_label");
            TextField depositAmountField = new TextField();
            depositAmountField.setPromptText("Enter deposit amount");
            depositAmountField.setPrefWidth(300);
            depositAmountField.setPrefHeight(35);
            formGrid.add(depositAmountLabel, 0, 2);
            formGrid.add(depositAmountField, 1, 2);

            //----- Deposit Button -----
            Button depositButton = new Button("Deposit");
            depositButton.getStyleClass().add("deposit_btn");
            depositButton.setPrefHeight(40);
            depositButton.setPrefWidth(150);
            GridPane.setMargin(depositButton, new Insets(10, 0, 0, 0));
            formGrid.add(depositButton, 1, 3);

            //----- Status Label For Feedback -----
            Label statusLabel = new Label("");
            statusLabel.getStyleClass().add("status_label");
            statusLabel.setVisible(false);
            formGrid.add(statusLabel, 0, 4, 3, 1);

            //----- Add Components To View -----
            depositsView.getChildren().addAll(titleLabel, formGrid);

            //----- Create Controller And Link UI Components -----
            DepositController controller = new DepositController();
            controller.pAddress_fld = payeeAddressField;
            controller.amount_fld = depositAmountField;
            controller.search_btn = searchButton;
            controller.result_listview = resultsListView;
            controller.deposit_btn = depositButton;

            //----- Custom Handling For Deposit Button To Fix The Dialog Issue -----
            depositButton.setOnAction(e -> {
                //----- Get The Selected Client -----
                Client selectedClient = resultsListView.getSelectionModel().getSelectedItem();
                if (selectedClient == null) {
                    showErrorDialog("Please Select A Client First");
                    return;
                }

                //----- Validate Deposit Amount -----
                String amountText = depositAmountField.getText();
                double amount;
                try {
                    amount = Double.parseDouble(amountText);
                    if (amount <= 0) {
                        showErrorDialog("Please Enter A Positive Amount");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    showErrorDialog("Please Enter A Valid Amount");
                    return;
                }

                //----- Show Deposit Confirmation Dialog -----
                boolean confirmed = showDepositConfirmationDialog(selectedClient, amount);
                if (confirmed) {
                    //----- Process The Deposit -----
                    processDeposit(selectedClient, amount, statusLabel);
                }
            });

            //----- Initialize Controller -----
            controller.initialize(null, null);

            return depositsView;

        } catch (Exception e) {
            e.printStackTrace();
            AnchorPane errorView = new AnchorPane();
            Label errorLabel = new Label("Error loading deposits view. Please contact support.");
            errorLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
            errorView.getChildren().add(errorLabel);
            AnchorPane.setTopAnchor(errorLabel, 10.0);
            AnchorPane.setLeftAnchor(errorLabel, 10.0);
            return errorView;
        }
    }

    //----- Shows An Error Dialog With The Specified Message -----
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //----- Shows A Deposit Confirmation Dialog And Returns True If Confirmed -----
    private boolean showDepositConfirmationDialog(Client client, double amount) {
        try {
            //----- Get Current Stage From The App -----
            Stage primaryStage = null;
            for (Stage stage : javafx.stage.Window.getWindows().filtered(window -> window instanceof Stage).stream().map(window -> (Stage) window).toList()) {
                if (stage.isShowing()) {
                    primaryStage = stage;
                    break;
                }
            }

            if (primaryStage == null) {
                //----- Fallback To Traditional Dialog If No Stage Is Found -----
                return showTraditionalConfirmDialog(client, amount);
            }

            //----- Create Overlay Pane -----
            StackPane overlayPane = new StackPane();
            overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

            //----- Create Dialog Content -----
            VBox dialogBox = new VBox(15);
            dialogBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 5; -fx-max-width: 400px;");
            dialogBox.setMaxWidth(400);
            dialogBox.setMaxHeight(300);
            dialogBox.setAlignment(Pos.CENTER);

            //----- Header Text -----
            Label headerLabel = new Label("Confirm Deposit");
            headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            //----- Client Info -----
            Label clientLabel = new Label("Client: " + client.firstNameProperty().get() + " " + client.lastNameProperty().get());
            clientLabel.setStyle("-fx-font-size: 14px;");

            Label amountLabel = new Label("Amount: $" + String.format("%.2f", amount));
            amountLabel.setStyle("-fx-font-size: 14px;");

            //----- Button Container -----
            HBox buttonBox = new HBox(20);
            buttonBox.setAlignment(Pos.CENTER);

            Button confirmButton = new Button("Confirm");
            confirmButton.setPrefWidth(100);
            confirmButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");

            Button cancelButton = new Button("Cancel");
            cancelButton.setPrefWidth(100);
            cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

            buttonBox.getChildren().addAll(confirmButton, cancelButton);

            //----- Add All Components To Dialog -----
            dialogBox.getChildren().addAll(headerLabel, clientLabel, amountLabel, buttonBox);

            //----- Add Dialog To Overlay -----
            overlayPane.getChildren().add(dialogBox);

            //----- Setup Content In The Scene -----
            Scene scene = primaryStage.getScene();
            Parent root = scene.getRoot();

            //----- Store The Result -----
            final boolean[] result = {false};

            if (root instanceof BorderPane) {
                BorderPane borderPane = (BorderPane) root;

                //----- Store Original Content -----
                Node originalCenter = borderPane.getCenter();

                //----- Create Content Pane With Original Content And Overlay -----
                StackPane contentPane = new StackPane();
                contentPane.getChildren().addAll(originalCenter, overlayPane);

                //----- Display The Overlay -----
                borderPane.setCenter(contentPane);

                //----- Create A CountDownLatch To Wait For Button Press -----
                CountDownLatch latch = new CountDownLatch(1);

                //----- Update The Button Actions To Release The Latch -----
                confirmButton.setOnAction(e -> {
                    result[0] = true;
                    borderPane.setCenter(originalCenter); 
                    latch.countDown();
                });

                cancelButton.setOnAction(e -> {
                    result[0] = false;
                    borderPane.setCenter(originalCenter);
                    latch.countDown();
                });

                //----- Wait On A Separate Thread To Avoid UI Freezing -----
                Thread waitThread = new Thread(() -> {
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                waitThread.setDaemon(true);
                waitThread.start();

                try {
                    //----- Wait For Thread To Complete With Timeout -----
                    waitThread.join(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //----- If Interrupted, Ensure We Restore The UI -----
                    Platform.runLater(() -> borderPane.setCenter(originalCenter));
                    return false;
                }

                return result[0];
            } else {
                //----- Fallback To Traditional Dialog -----
                return showTraditionalConfirmDialog(client, amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //----- Fallback To Traditional Dialog In Case Of Errors -----
            return showTraditionalConfirmDialog(client, amount);
        }
    }

    //----- Fallback Method To Show A Traditional Confirmation Dialog -----
    private boolean showTraditionalConfirmDialog(Client client, double amount) {
        //----- Create A Small, Properly Sized Dialog -----
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setTitle("Confirm Deposit");
        dialogStage.setResizable(false);

        //----- Create Content For The Dialog -----
        VBox dialogRoot = new VBox(15);
        dialogRoot.setPadding(new Insets(20));
        dialogRoot.setAlignment(Pos.CENTER);
        dialogRoot.setPrefWidth(400);

        //----- Header Text -----
        Label headerLabel = new Label("Confirm Deposit");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        //----- Client Info -----
        Label clientLabel = new Label("Client: " + client.firstNameProperty().get() + " " + client.lastNameProperty().get());
        clientLabel.setStyle("-fx-font-size: 14px;");

        Label amountLabel = new Label("Amount: $" + String.format("%.2f", amount));
        amountLabel.setStyle("-fx-font-size: 14px;");

        //----- Button Container -----
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button confirmButton = new Button("Confirm");
        confirmButton.setPrefWidth(100);
        confirmButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(100);
        cancelButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        buttonBox.getChildren().addAll(confirmButton, cancelButton);

        //----- Add All Components To Dialog Root -----
        dialogRoot.getChildren().addAll(headerLabel, clientLabel, amountLabel, buttonBox);

        //----- Set Up The Scene -----
        Scene scene = new Scene(dialogRoot);
        dialogStage.setScene(scene);

        //----- Set Up Button Actions -----
        final boolean[] result = {false}; // To store the result

        confirmButton.setOnAction(e -> {
            result[0] = true;
            dialogStage.close();
        });

        cancelButton.setOnAction(e -> {
            result[0] = false;
            dialogStage.close();
        });

        //----- Show Dialog And Wait For It To Close -----
        dialogStage.showAndWait();

        return result[0];
    }

    //----- Processes The Deposit For A Client -----
    private boolean processDeposit(Client client, double amount, Label statusLabel) {
        try {
            String payeeAddress = client.payeeAddressProperty().get();
            String accountNumber = "";

            //----- Get The Account Number From The Client's Savings Account -----
            if (client.savingsAccountProperty().get() != null) {
                accountNumber = client.savingsAccountProperty().get().accountNumberProperty().get();
            } else {
                //----- Display Error If Client Doesn't Have A Savings Account -----
                statusLabel.setText("Client has no savings account");
                statusLabel.setVisible(true);
                return false;
            }

            //----- Use The Service Layer To Process The Deposit -----
            boolean success = Model.getInstance().createDeposit(
                    payeeAddress,
                    accountNumber,
                    "savings",
                    amount,
                    "Admin deposit"
            );

            if (success) {
                //----- Update UI With Success Message -----
                statusLabel.setText("Deposit successful!");
                statusLabel.setVisible(true);
                return true;
            } else {
                //----- Update UI With Error Message -----
                statusLabel.setText("Deposit failed. Please try again.");
                statusLabel.setVisible(true);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setVisible(true);
            return false;
        }
    }
} 
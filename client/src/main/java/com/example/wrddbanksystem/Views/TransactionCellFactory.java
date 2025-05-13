package com.example.wrddbanksystem.Views;

import com.example.wrddbanksystem.Core.Model;
import com.example.wrddbanksystem.Models.Transaction;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Button;
import javafx.stage.Popup;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.util.Duration;
import javafx.scene.paint.Paint;
import javafx.stage.Window;
import javafx.scene.control.Separator;

import java.time.format.DateTimeFormatter;

//----- Class To Create A Custom Cell For The Transaction List -----
public class TransactionCellFactory extends ListCell<Transaction> {

    //----- Updates The Item In The List -----
    @Override
    protected void updateItem(Transaction transaction, boolean empty) {
        super.updateItem(transaction, empty);

        if (empty || transaction == null) {
            setText(null);
            setGraphic(null);
        } else {
            //----- Creates The Cell Root -----
            try {
                AnchorPane cellRoot = new AnchorPane();
                cellRoot.setPrefHeight(65.0);
                cellRoot.setPrefWidth(650.0);
                cellRoot.getStyleClass().add("cell_container");
                cellRoot.getStylesheets().add(getClass().getResource("/Styles/transactioncell.css").toExternalForm());

                FontAwesomeIconView transactionIcon = new FontAwesomeIconView();
                transactionIcon.setSize("18");

                //----- Sets The Transaction Icon -----
                if (transaction.getSender().equals(Model.getInstance().getClient().payeeAddressProperty().get()) &&
                        transaction.getReceiver().equals(Model.getInstance().getClient().payeeAddressProperty().get())) {
                    transactionIcon.setIcon(FontAwesomeIcon.EXCHANGE);
                    transactionIcon.getStyleClass().add("arrow-exchange");
                } else if (transaction.getReceiver().equals(Model.getInstance().getClient().payeeAddressProperty().get())) {
                    //----- Sets The Transaction Icon To The Arrow Right Icon -----
                    transactionIcon.setIcon(FontAwesomeIcon.ARROW_RIGHT);
                    transactionIcon.getStyleClass().add("arrow-right");
                } else {
                    //----- Sets The Transaction Icon To The Arrow Left Icon -----
                    transactionIcon.setIcon(FontAwesomeIcon.ARROW_LEFT);
                    transactionIcon.getStyleClass().add("arrow-left");
                }

                //----- Sets The Transaction Icon To The Left Anchor -----
                AnchorPane.setLeftAnchor(transactionIcon, 12.0);
                //----- Sets The Transaction Icon To The Top Anchor -----
                AnchorPane.setTopAnchor(transactionIcon, 22.0);

                VBox transactionInfo = new VBox(3);
                AnchorPane.setLeftAnchor(transactionInfo, 45.0);
                AnchorPane.setTopAnchor(transactionInfo, 12.0);     

                //----- Formats The Date -----
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String dateText = transaction.getDate().format(formatter);

                //----- Creates The Address Label -----
                String addressText;
                if (transaction.getSender().equals(Model.getInstance().getClient().payeeAddressProperty().get())) {
                    //----- Sent To Someone Else -----
                    addressText = "To: " + transaction.getReceiver();
                } else {
                    //----- Received From Someone Else -----
                    addressText = "From: " + transaction.getSender();
                }

                Text addressLabel = new Text(addressText);
                addressLabel.getStyleClass().add("trans_pAddress");

                //----- Creates The Date Label -----
                Text dateLabel = new Text(dateText);
                dateLabel.getStyleClass().add("trans_date");

                transactionInfo.getChildren().addAll(addressLabel, dateLabel);

                //----- Creates The Amount Label -----
                Label transAmount = new Label("$" + transaction.getAmount());
                transAmount.getStyleClass().add("trans_amount");

                //----- Sets The Color For The Amount Based On The Transaction Type -----
                if (transaction.getSender().equals(Model.getInstance().getClient().payeeAddressProperty().get()) &&
                        !transaction.getReceiver().equals(Model.getInstance().getClient().payeeAddressProperty().get())) {
                    //----- Money Sent Out - Red Color -----
                    transAmount.setTextFill(Paint.valueOf("#e74c3c"));
                    transAmount.setText("-$" + transaction.getAmount());
                } else if (transaction.getReceiver().equals(Model.getInstance().getClient().payeeAddressProperty().get()) &&
                        !transaction.getSender().equals(Model.getInstance().getClient().payeeAddressProperty().get())) {
                    //----- Money Received - Green Color -----
                    transAmount.setTextFill(Paint.valueOf("#27ae60"));
                    transAmount.setText("+$" + transaction.getAmount());
                } else {
                    //----- Internal Transfer - Blue Color -----
                    transAmount.setTextFill(Paint.valueOf("#3498db"));
                }

                //----- Fixes The Position Of The Amount -----
                AnchorPane.setRightAnchor(transAmount, 45.0);
                AnchorPane.setTopAnchor(transAmount, 22.0);

                //----- Creates The Message Button If The Transaction Has A Message -----
                if (transaction.getMessage() != null && !transaction.getMessage().isEmpty()) {
                    Button msgButton = new Button();
                    msgButton.getStyleClass().add("message_btn");

                    FontAwesomeIconView messageIcon = new FontAwesomeIconView(FontAwesomeIcon.BELL);
                    messageIcon.setSize("14");
                    msgButton.setGraphic(messageIcon);

                    AnchorPane.setRightAnchor(msgButton, 15.0);
                    AnchorPane.setTopAnchor(msgButton, 22.0);

                    //----- Creates The Tooltip For The Message Preview -----
                    Tooltip messageTooltip = new Tooltip("Message: " + transaction.getMessage());
                    messageTooltip.setShowDelay(Duration.millis(300));
                    Tooltip.install(msgButton, messageTooltip);

                    //----- Creates The Popup For The Message Display -----
                    Popup messagePopup = new Popup();
                    messagePopup.setAutoHide(true);

                    //----- Creates The Popup Content -----
                    VBox popupContent = new VBox(5);
                    popupContent.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-padding: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 3);");
                    popupContent.setPrefWidth(280);

                    Text popupTitle = new Text("Message");
                    popupTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

                    Text senderText = new Text("From: " + transaction.getSender());
                    senderText.setStyle("-fx-font-size: 12;");

                    Label messageLabel = new Label(transaction.getMessage());
                    messageLabel.setWrapText(true);
                    messageLabel.setPrefWidth(280);
                    messageLabel.setStyle("-fx-font-size: 13;");

                    Button closeButton = new Button("Close");
                    closeButton.setPrefWidth(100);
                    closeButton.setStyle("-fx-background-color: #eeeeee; -fx-text-fill: #333333; -fx-cursor: hand;");
                    closeButton.setOnAction(e -> messagePopup.hide());

                    HBox buttonBox = new HBox();
                    buttonBox.setAlignment(Pos.CENTER_RIGHT);
                    buttonBox.getChildren().add(closeButton);

                    popupContent.getChildren().addAll(popupTitle, senderText, new Separator(), messageLabel, buttonBox);
                    messagePopup.getContent().add(popupContent);

                    //----- Shows The Popup On Button Click -----
                    msgButton.setOnAction(e -> {
                        Window window = getScene().getWindow();
                        messagePopup.show(window,
                                window.getX() + msgButton.localToScreen(msgButton.getBoundsInLocal()).getMinX(),
                                window.getY() + msgButton.localToScreen(msgButton.getBoundsInLocal()).getMinY() - 120);
                    });

                    cellRoot.getChildren().add(msgButton);
                }

                //----- Adds All Components To The Cell -----
                cellRoot.getChildren().addAll(transactionIcon, transactionInfo, transAmount);

                //----- Applies The Styles -----
                String cssPath = getClass().getResource("/Styles/transactioncell.css").toExternalForm();
                cellRoot.getStylesheets().add(cssPath);

                //----- Disables Mouse Events To Prevent Shifting -----
                cellRoot.setMouseTransparent(false);

                setGraphic(cellRoot);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error creating transaction cell: " + e.getMessage());
                setText("Error loading transaction cell");
            }
        }
    }
}

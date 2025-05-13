package com.example.wrddbanksystem.Views;

import com.example.wrddbanksystem.Controllers.Admin.ClientCellController;
import com.example.wrddbanksystem.Models.Client;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

//----- Class To Create A Custom Cell For The Client List -----
public class ClientCellFactory extends ListCell<Client> {
    @Override
    protected void updateItem(Client client, boolean isEmpty) {
        super.updateItem(client, isEmpty);
        if (isEmpty) {
            setText(null);
            setGraphic(null);
        } else {
            try {
                //----- Create Cell UI -----
                AnchorPane cellRoot = new AnchorPane();
                cellRoot.setPrefHeight(76.0);
                cellRoot.setPrefWidth(781.0);
                cellRoot.getStyleClass().add("client_cell_container");
                
                //----- User Icon -----
                FontAwesomeIconView userIcon = new FontAwesomeIconView(FontAwesomeIcon.USER);
                userIcon.setSize("25");
                userIcon.getStyleClass().add("client_cell_icon");
                AnchorPane.setLeftAnchor(userIcon, 14.0);
                AnchorPane.setTopAnchor(userIcon, 26.0);
                
                //----- Client Info Section -----
                VBox clientInfoBox = new VBox(4);
                
                //----- Client Name -----
                Label nameLabel = new Label(client.firstNameProperty().get() + " " + client.lastNameProperty().get());
                nameLabel.getStyleClass().add("client_cell_name");
                
                //----- Client Payee Address -----
                Label addressLabel = new Label(client.payeeAddressProperty().get());
                addressLabel.getStyleClass().add("client_cell_address");
                
                clientInfoBox.getChildren().addAll(nameLabel, addressLabel);
                AnchorPane.setLeftAnchor(clientInfoBox, 50.0);
                AnchorPane.setTopAnchor(clientInfoBox, 18.0);
                
                //----- Balance Display -----
                Label balanceLabel = new Label();
                if (client.checkingAccountProperty().get() != null) {
                    balanceLabel.setText("$" + client.checkingAccountProperty().get().balanceProperty().get());
                } else if (client.savingsAccountProperty().get() != null) {
                    balanceLabel.setText("$" + client.savingsAccountProperty().get().balanceProperty().get());
                } else {
                    balanceLabel.setText("$0.00");
                }
                balanceLabel.getStyleClass().add("client_cell_balance");
                AnchorPane.setRightAnchor(balanceLabel, 14.0);
                AnchorPane.setTopAnchor(balanceLabel, 26.0);
                
                //----- Add UI Elements To The Cell -----
                cellRoot.getChildren().addAll(userIcon, clientInfoBox, balanceLabel);
                
                //----- Create Controller And Connect It To UI Elements -----
                ClientCellController controller = new ClientCellController(client);
                
                //----- Apply Styles -----
                String cssPath = getClass().getResource("/Styles/clientcell.css").toExternalForm();
                cellRoot.getStylesheets().add(cssPath);
                
                setGraphic(cellRoot);
            } catch (Exception e) {
                e.printStackTrace();
                setText("Error creating client cell");
            }
        }
    }
}

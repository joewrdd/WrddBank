package com.example.wrddbanksystem.Controllers.Admin;

import com.example.wrddbanksystem.Models.Client;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

//----- Initialize The Client Cell Controller -----
public class ClientCellController  {
    public Label fName_lbl;
    public Label lName_lbl;
    public Label pAddress_lbl;
    public Label ch_acc_lbl;
    public Label sv_acc_lbl;
    public Label date_lbl;
    public Button delete_btn;

    private final Client client;

    //----- Initialize The Client Cell Controller -----
    public ClientCellController(Client client) {
        this.client = client;
    }

    //----- Initialize The Client Cell Controller -----
    
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fName_lbl.textProperty().bind(client.firstNameProperty());
        lName_lbl.textProperty().bind(client.lastNameProperty());
        pAddress_lbl.textProperty().bind(client.payeeAddressProperty());
        ch_acc_lbl.textProperty().bind(client.checkingAccountProperty().asString());
        sv_acc_lbl.textProperty().bind(client.savingsAccountProperty().asString());
        date_lbl.textProperty().bind(client.dateCreatedProperty().asString());
    }
}

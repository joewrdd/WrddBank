package com.example.wrddbanksystem.Controllers.Admin;

import com.example.wrddbanksystem.Models.Client;
import com.example.wrddbanksystem.Core.Model;
import com.example.wrddbanksystem.Views.ClientCellFactory;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

//----- Initialize The All Clients Controller -----
public class AllClientsController {
    public ListView<Client> all_clients_listview;

    //----- Initialize The All Clients Controller -----
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initClientsList();
        all_clients_listview.setItems(Model.getInstance().getClients());
        all_clients_listview.setCellFactory(e -> new ClientCellFactory());
    }

    //----- Method To Initialize The Clients List ----- 
    private void initClientsList() {
        if (Model.getInstance().getClients().isEmpty()) {
            Model.getInstance().setClients();
        }
    }
}

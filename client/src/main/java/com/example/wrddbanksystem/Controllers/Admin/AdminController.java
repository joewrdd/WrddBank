package com.example.wrddbanksystem.Controllers.Admin;

import com.example.wrddbanksystem.Core.Model;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

//----- Admin Controller -----
public class AdminController {
    public BorderPane admin_parent;

    //----- Initialize The Admin Controller -----
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case DASHBOARD -> admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminDashboardView());
                case CLIENTS -> admin_parent.setCenter(Model.getInstance().getViewFactory().getAllClientsView());
                case DEPOSIT -> admin_parent.setCenter(Model.getInstance().getViewFactory().getDepositsView());
                default -> admin_parent.setCenter(Model.getInstance().getViewFactory().getCreateClientView());
            }
        });
    }
}

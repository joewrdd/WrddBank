package com.example.wrddbanksystem.Controllers.Admin;

import com.example.wrddbanksystem.Core.Model;
import com.example.wrddbanksystem.Views.AdminMenuOptions;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

//----- Initialize The Admin Menu Controller -----
public class AdminMenuController {
    public Button dashboard_btn;
    public Button create_client_btn;
    public Button clients_btn;
    public Button deposit_btn;
    public Button logout_btn;

    //----- Initialize The Admin Menu Controller -----
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    //----- Method To Add Listeners -----
    private void addListeners() {
        dashboard_btn.setOnAction(e -> onDashboard());
        create_client_btn.setOnAction(e -> onCreateClient());
        clients_btn.setOnAction(e -> onAllClients());
        deposit_btn.setOnAction(e -> onDeposit());
        logout_btn.setOnAction(e -> onLogout());
    }

    //----- Method To Navigate To The Dashboard -----
    private void onDashboard() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DASHBOARD);
    }

    //----- Method To Navigate To The Create Client -----
    private void onCreateClient() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CREATE_CLIENT);
    }

    //----- Method To Navigate To The All Clients -----
    private void onAllClients() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTS);
    }

    //----- Method To Navigate To The Deposit -----
    private void onDeposit() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DEPOSIT);
    }

    //----- Method To Logout -----
    private void onLogout() {
        try {
            //----- Get Stage -----
            Stage currentStage = (Stage) clients_btn.getScene().getWindow();

            //----- Reset Admin Login Flag -----
            Model.getInstance().setAdminLoginSuccessFlag(false);

            //----- Clear All Cached Views -----
            Model.getInstance().getViewFactory().clearCachedViews();

            //----- Show Login Window -----
            Model.getInstance().getViewFactory().showLoginWindow();

            //----- Close Current Stage After a Short Delay to Ensure Login Window is Properly Shown -----
            javafx.application.Platform.runLater(() -> {
                try {
                    Model.getInstance().getViewFactory().closeStage(currentStage);
                } catch (Exception e) {
                    System.err.println("Error closing admin stage: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


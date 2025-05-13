package com.example.wrddbanksystem.Controllers.Client;

import com.example.wrddbanksystem.Core.Model;
import com.example.wrddbanksystem.Views.ClientMenuOptions;
// No longer using FXML Initializable interface - implementing initialize method directly
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController {
    public Button dashboard_btn;
    public Button transaction_btn;
    public Button accounts_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;

    //----- Initialize The Client Menu Controller -----

    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    //----- Method To Add Listeners -----
    private void addListeners() {
        dashboard_btn.setOnAction(event -> onDashboard());
        transaction_btn.setOnAction(event -> onTransactions());
        accounts_btn.setOnAction(event -> onAccounts());
        profile_btn.setOnAction(event -> onProfile());
        logout_btn.setOnAction(event -> onLogout());
    }

    //----- Method To Navigate To Dashboard -----
    private void onDashboard() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
    }

    //----- Method To Navigate To Transactions -----
    private void onTransactions() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.TRANSACTIONS);
    }

    //----- Method To Navigate To Accounts -----
    private void onAccounts() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.ACCOUNTS);
    }

    //----- Method To Navigate To Profile -----
    private void onProfile() {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.PROFILE);
    }

    //----- Method To Logout -----
    private void onLogout() {
        try {
            //----- Get Stage -----
            Stage currentStage = (Stage) dashboard_btn.getScene().getWindow();

            //----- Reset Client Login Flag -----
            Model.getInstance().setClientLoginSuccessFlag(false);

            //----- Clear All Cached Views To Prevent Stale Data -----
            Model.getInstance().getViewFactory().clearCachedViews();

            //----- Show Login Window -----
            Model.getInstance().getViewFactory().showLoginWindow();

            //----- Close Current Stage After A Short Delay To Ensure Login Window Is Properly Shown -----
            javafx.application.Platform.runLater(() -> {
                try {
                    Model.getInstance().getViewFactory().closeStage(currentStage);
                } catch (Exception e) {
                    System.err.println("Error closing client stage: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

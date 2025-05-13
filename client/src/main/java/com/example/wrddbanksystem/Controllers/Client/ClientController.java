package com.example.wrddbanksystem.Controllers.Client;

import com.example.wrddbanksystem.Core.Model;
// No longer using FXML Initializable interface - implementing initialize method directly
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController {
    public BorderPane client_parent;

    //----- Initialize The Client Controller -----
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case TRANSACTIONS -> {
                    client_parent.setCenter(Model.getInstance().getViewFactory().getTransactionsView());
                    //----- Refresh Transactions When Navigating To This View -----
                    Model.getInstance().setLatestTransactions();
                }
                case ACCOUNTS -> {
                    //----- Refresh Accounts Data Before Showing Accounts View -----
                    Model.getInstance().refreshAccounts();

                    //----- Get The Accounts View And Set It As Center -----
                    AnchorPane accountsView = Model.getInstance().getViewFactory().getAccountsView();
                    client_parent.setCenter(accountsView);

                    //----- Access The Controller And Explicitly Refresh The Data -----
                    AccountController controller = (AccountController) accountsView.getUserData();
                    if (controller != null) {
                        controller.refreshAccountData();
                    }
                }
                case PROFILE -> {
                    client_parent.setCenter(Model.getInstance().getViewFactory().getProfileView());
                    ProfileController controller = (ProfileController) Model.getInstance()
                            .getViewFactory().getProfileView().getUserData();
                    controller.refreshData();
                }
                default -> {
                    //----- Always Refresh Account Data When Returning To Dashboard -----
                    Model.getInstance().refreshAccounts();

                    //----- Clear The Existing Dashboard View To Force A Refresh -----
                    Model.getInstance().getViewFactory().clearDashboardView();

                    //----- Set The Center To The Freshly Created Dashboard View -----
                    client_parent.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                }
            }
        });
    }
}

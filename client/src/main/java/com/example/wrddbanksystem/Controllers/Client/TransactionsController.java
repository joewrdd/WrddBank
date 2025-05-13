package com.example.wrddbanksystem.Controllers.Client;

import com.example.wrddbanksystem.Core.Model;
import com.example.wrddbanksystem.Models.Transaction;
import com.example.wrddbanksystem.Views.TransactionCellFactory;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

//----- Transactions Controller -----
public class TransactionsController {
    public ListView<Transaction> transactions_listview;

    //----- Initialize The Transactions Controller -----
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //----- Always Refresh All Transactions To Get The Latest Data -----
        refreshTransactions();
        transactions_listview.setItems(Model.getInstance().getAllTransactions());
        transactions_listview.setCellFactory(e -> new TransactionCellFactory());
    }

    //----- Method To Refresh Transactions -----
    private void refreshTransactions() {
        //----- Always Reload All Transactions To Get Fresh Data -----
        Model.getInstance().setAllTransactions();
    }
}

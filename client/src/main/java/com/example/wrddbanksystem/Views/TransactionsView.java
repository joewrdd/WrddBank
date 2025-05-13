package com.example.wrddbanksystem.Views;

import com.example.wrddbanksystem.Controllers.Client.TransactionsController;
import com.example.wrddbanksystem.Models.Transaction;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

    //----- Creates The Transactions View -----
public class TransactionsView {
    
   //----- Creates The Transactions View -----
    public AnchorPane createView() {
        //----- Creates The Transactions View -----
        AnchorPane transactionsView = new AnchorPane();
        transactionsView.setPrefHeight(750.0);
        transactionsView.setPrefWidth(1000.0);
        transactionsView.getStyleClass().add("transactions_container");
        transactionsView.getStylesheets().add(getClass().getResource("/Styles/transactions.css").toExternalForm());

        //----- Creates The Transactions Controller -----
        TransactionsController transactionsController = new TransactionsController();

        //----- Creates The Title Text -----
        Text titleText = new Text("Transactions");
        AnchorPane.setLeftAnchor(titleText, 349.0);
        AnchorPane.setTopAnchor(titleText, 14.0);

        //----- Creates The Transactions List View -----
        ListView<Transaction> transactionsListView = new ListView<>();
        transactionsListView.setPrefHeight(640.0);
        transactionsListView.setPrefWidth(900.0);
        AnchorPane.setLeftAnchor(transactionsListView, 14.0);
        AnchorPane.setTopAnchor(transactionsListView, 100.0);

        //----- Prevents Horizontal Movement And Makes Cells Stable -----
        transactionsListView.setFixedCellSize(65.0); 
        transactionsListView.setPrefWidth(900.0); 
        transactionsListView.setMinWidth(900.0);
        transactionsListView.setMaxWidth(900.0);

        transactionsController.transactions_listview = transactionsListView;

        //----- Adds The Components To The Transactions View -----
        transactionsView.getChildren().addAll(titleText, transactionsListView);

        //----- Initializes The Controller -----
        transactionsController.initialize(null, null);

        return transactionsView;
    }
} 
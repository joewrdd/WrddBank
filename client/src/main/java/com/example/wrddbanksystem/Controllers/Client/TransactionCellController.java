package com.example.wrddbanksystem.Controllers.Client;

import com.example.wrddbanksystem.Core.Model;
import com.example.wrddbanksystem.Models.Transaction;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

//----- Transaction Cell Controller -----
public class TransactionCellController {
    public FontAwesomeIconView in_icon;
    public FontAwesomeIconView out_icon;
    public Label trans_date_lbl;
    public Label sender_lbl;
    public Label receiver_lbl;
    public Label amount_lbl;
    public Button message_btn;
    public AnchorPane transaction_cell_container;

    private final Transaction transaction;

    //----- Constructor -----
    public TransactionCellController(Transaction transaction) {
        this.transaction = transaction;
    }

    //----- Initialize -----
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String currentUserPayee = Model.getInstance().getClient().payeeAddressProperty().get();

        sender_lbl.textProperty().bind(transaction.senderProperty());
        receiver_lbl.textProperty().bind(transaction.receiverProperty());
        amount_lbl.textProperty().bind(transaction.amountProperty().asString());
        trans_date_lbl.textProperty().bind(transaction.dateProperty().asString());

        message_btn.setOnAction(event -> {
            Model.getInstance().getViewFactory().showDialogMessage(transaction.senderProperty().get(),
                    transaction.messageProperty().get());
        });

        boolean isUserTransaction = transaction.senderProperty().get().equals(currentUserPayee) ||
                transaction.receiverProperty().get().equals(currentUserPayee);

        if (!isUserTransaction) {

            System.err.println("WARNING: Displaying transaction that doesn't involve current user: " +
                    transaction.senderProperty().get() + " -> " +
                    transaction.receiverProperty().get());

            if (transaction_cell_container != null) {
                transaction_cell_container.setStyle("-fx-background-color: #ffeeee;");
            }
        }

        transactionIcons();
    }

    //----- Transaction Icons -----
    private void transactionIcons() {
        String currentUserPayee = Model.getInstance().getClient().payeeAddressProperty().get();

        if (transaction.senderProperty().get().equals(currentUserPayee)) {
            in_icon.setFill(Color.rgb(240, 240, 240));
            out_icon.setFill(Color.RED);
        } else if (transaction.receiverProperty().get().equals(currentUserPayee)) {
            out_icon.setFill(Color.rgb(240, 240, 240));
            in_icon.setFill(Color.GREEN);
        } else {
            in_icon.setFill(Color.GRAY);
            out_icon.setFill(Color.GRAY);
        }
    }
}

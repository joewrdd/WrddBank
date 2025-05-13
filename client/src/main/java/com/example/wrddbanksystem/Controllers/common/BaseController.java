package com.example.wrddbanksystem.Controllers.common;

import com.example.wrddbanksystem.Core.Model;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;


//----- A Base Controller For All Controllers -----
public abstract class BaseController {

    //----- Reference To The Global Application Model -----
    protected final Model model;

    //----- Loading Indicator For Async Operations -----
    private ProgressIndicator loadingIndicator;

    //----- Constructor Initializes The Model Reference -----
    public BaseController() {
        this.model = Model.getInstance();
    }

    //----- Shows An Error Alert With The Specified Title And Content -----
    protected void showErrorAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText("Error");
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    //----- Shows A Success Alert With The Specified Title And Content -----
    protected void showSuccessAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText("Success");
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    //----- Shows A Confirmation Dialog And Returns The User's Choice -----
    protected boolean showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    //----- Executes An Asynchronous Operation With Loading Indicator -----
    protected <T> CompletableFuture<T> executeWithLoading(StackPane container, Supplier<T> operation) {
        if (loadingIndicator == null) {
            loadingIndicator = new ProgressIndicator();
            loadingIndicator.setMaxSize(100, 100);
        }

        //----- Add Loading Indicator If Not Already Present -----
        if (!container.getChildren().contains(loadingIndicator)) {
            Platform.runLater(() -> container.getChildren().add(loadingIndicator));
        }

        //----- Execute Operation Asynchronously -----
        return CompletableFuture.supplyAsync(operation)
                .whenComplete((result, ex) -> {
                    Platform.runLater(() -> container.getChildren().remove(loadingIndicator));

                    if (ex != null) {
                        showErrorAlert("Operation Failed", "An error occurred: " + ex.getMessage());
                    }
                });
    }

    //----- Executes An Asynchronous Operation With Disabled Control -----
    protected <T> CompletableFuture<T> executeWithDisabled(Control control, Supplier<T> operation) {
        boolean wasDisabled = control.isDisabled();
        control.setDisable(true);

        return CompletableFuture.supplyAsync(operation)
                .whenComplete((result, ex) -> {
                    Platform.runLater(() -> control.setDisable(wasDisabled));

                    if (ex != null) {
                        showErrorAlert("Operation Failed", "An error occurred: " + ex.getMessage());
                    }
                });
    }

    //----- Sets A Status Message With The Specified Color -----
    protected void setStatusMessage(javafx.scene.control.Label label, String message, boolean isError) {
        Platform.runLater(() -> {
            label.setText(message);

            if (isError) {
                label.setTextFill(Color.RED);
                label.setStyle("-fx-text-fill: red;");
            } else {
                label.setTextFill(Color.GREEN);
                label.setStyle("-fx-text-fill: green;");
            }

            //----- Make The Label Visible -----
            label.setVisible(true);
        });
    }

    //----- Clears A Status Message -----
    protected void clearStatusMessage(javafx.scene.control.Label label) {
        Platform.runLater(() -> {
            label.setText("");
            label.setVisible(false);
        });
    }
} 
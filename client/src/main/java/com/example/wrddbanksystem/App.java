package com.example.wrddbanksystem;

import com.example.wrddbanksystem.Core.Model;
import javafx.application.Application;
import javafx.stage.Stage;

//----- Main Class -----
public class App extends Application {
    @Override
    public void start(Stage stage) {
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("javafx.animation.fullspeed", "true");
        System.setProperty("prism.verbose", "true");
        System.setProperty("javafx.verbose", "true");

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("Uncaught Exception On Thread " + thread);
            throwable.printStackTrace();
        });

        Model.getInstance().getViewFactory().showLoginWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
 
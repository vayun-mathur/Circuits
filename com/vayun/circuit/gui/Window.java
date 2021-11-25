package com.vayun.circuit.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Circuits");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        //stage.setMinWidth(stage.getWidth());
        //stage.setMinHeight(stage.getHeight());
    }

    public static void run() {
        launch();
    }
}

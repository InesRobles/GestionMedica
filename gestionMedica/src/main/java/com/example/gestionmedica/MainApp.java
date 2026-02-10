package com.example.gestionmedica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // OJO: esta ruta es desde resources
        String fxmlPath = "/com/example/gestionmedica/hello-view.fxml";

        URL url = getClass().getResource(fxmlPath);
        Objects.requireNonNull(url, "No se encuentra el FXML en: " + fxmlPath);

        FXMLLoader loader = new FXMLLoader(url);
        Scene scene = new Scene(loader.load(), 400, 300);

        stage.setTitle("Gestión Médica (UI)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

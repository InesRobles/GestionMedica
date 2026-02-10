package com.example.gestionmedica;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import com.example.gestionmedica.utils.DatabaseConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación de Gestión Médica
 * Punto de entrada del sistema
 */
public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Verificar conexión a la base de datos al iniciar
        verificarConexionDB();

        // Cargar la vista principal (login)
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 350);

        // Configurar la ventana principal
        stage.setTitle("Sistema de Gestión Médica - Hospital MVP");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Cambia la escena actual por otra vista FXML
     * @param fxmlFile Nombre del archivo FXML (debe estar en resources/com/example/gestionmedica/)
     * @param titulo Título de la ventana
     * @param ancho Ancho de la escena
     * @param alto Alto de la escena
     */
    public static void cambiarEscena(String fxmlFile, String titulo, double ancho, double alto) throws IOException {
        URL url = MainApp.class.getResource(fxmlFile);
        Objects.requireNonNull(url, "No se encuentra el FXML: " + fxmlFile);

        FXMLLoader loader = new FXMLLoader(url);
        Scene scene = new Scene(loader.load(), ancho, alto);
        primaryStage.setTitle(titulo);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Verifica la conexión a la base de datos al iniciar
     */
    private void verificarConexionDB() {
        try {
            DatabaseConnection.testConnection();
            System.out.println("✓ Conexión a la base de datos verificada");
        } catch (Exception e) {
            System.err.println("✗ No se pudo conectar a la base de datos: " + e.getMessage());
            System.err.println("  La aplicación continuará, pero las operaciones de BD fallarán.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

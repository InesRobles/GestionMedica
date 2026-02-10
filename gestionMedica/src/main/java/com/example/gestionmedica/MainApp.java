package com.example.gestionmedica;

import java.io.IOException;

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
        
        // Cargar la vista principal
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 350);
        
        // Configurar la ventana principal
        stage.setTitle("Sistema de Gestión Médica - Hospital MVP");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        
        // Opcional: Agregar un ícono a la aplicación
        // stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("icon.png")));
        
        stage.show();
        
        System.out.println("✓ Aplicación iniciada correctamente");
    }

    /**
     * Verifica la conexión a la base de datos al iniciar la aplicación
     */
    private void verificarConexionDB() {
        System.out.println("========================================");
        System.out.println("  SISTEMA DE GESTIÓN MÉDICA - v1.0");
        System.out.println("========================================");
        
        try {
            DatabaseConnection.testConnection();
        } catch (Exception e) {
            System.err.println("⚠ ADVERTENCIA: No se pudo conectar a la base de datos");
            System.err.println("  La aplicación se iniciará, pero las funciones de BD no estarán disponibles");
            System.err.println("  Error: " + e.getMessage());
        }
        
        System.out.println("========================================\n");
    }

    /**
     * Método para cambiar de escena desde otros controladores
     * @param fxmlFile Nombre del archivo FXML (ej: "Dashboard.fxml")
     * @param title Título de la ventana
     * @throws IOException Si no se puede cargar el archivo FXML
     */
    public static void cambiarEscena(String fxmlFile, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
    }

    /**
     * Método para cambiar de escena con tamaño específico
     * @param fxmlFile Nombre del archivo FXML
     * @param title Título de la ventana
     * @param width Ancho de la ventana
     * @param height Alto de la ventana
     * @throws IOException Si no se puede cargar el archivo FXML
     */
    public static void cambiarEscena(String fxmlFile, String title, int width, int height) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
    }

    /**
     * Obtiene el Stage principal de la aplicación
     * @return Stage principal
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Cierra la aplicación y la conexión a la base de datos
     */
    @Override
    public void stop() {
        System.out.println("\n========================================");
        System.out.println("  Cerrando aplicación...");
        DatabaseConnection.closeConnection();
        System.out.println("  ¡Hasta pronto!");
        System.out.println("========================================");
    }

    /**
     * Método main - Punto de entrada de la aplicación
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}

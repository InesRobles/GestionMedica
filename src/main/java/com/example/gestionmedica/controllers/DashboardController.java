package com.example.gestionmedica.controllers;

import com.example.gestionmedica.MainApp;
import com.example.gestionmedica.models.Usuario;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

/**
 * Controlador para el dashboard principal
 */
public class DashboardController {
    
    @FXML
    private Label labelUsuario;
    
    @FXML
    private Label labelEstadoConexion;
    
    @FXML
    public void initialize() {
        System.out.println("✓ DashboardController inicializado");
        
        // Mostrar información del usuario actual
        Usuario usuarioActual = HelloController.getUsuarioActual();
        if (usuarioActual != null && labelUsuario != null) {
            labelUsuario.setText("Usuario: " + usuarioActual.getNombreCompleto() + 
                               " (" + usuarioActual.getRol() + ")");
        }
    }
    
    @FXML
    private void irAPacientes() {
        System.out.println("Navegando a Pacientes...");
        try {
            MainApp.cambiarEscena("Pacientes.fxml", "Gestión de Pacientes", 1000, 700);
        } catch (Exception e) {
            System.err.println("✗ Error al cargar vista de Pacientes: " + e.getMessage());
            mostrarAlerta("Error", "La vista de Pacientes aún no está disponible", Alert.AlertType.INFORMATION);
        }
    }
    
    @FXML
    private void irAMedicos() {
        System.out.println("Navegando a Médicos...");
        try {
            MainApp.cambiarEscena("VistaDoctor.fxml", "Gestión de Médicos", 1000, 700);
        } catch (Exception e) {
            System.err.println("✗ Error al cargar vista de Médicos: " + e.getMessage());
            mostrarAlerta("Error", "Error al cargar la vista de Médicos", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void irACitas() {
        System.out.println("Navegando a Citas...");
        try {
            MainApp.cambiarEscena("Citas.fxml", "Gestión de Citas", 1000, 700);
        } catch (Exception e) {
            System.err.println("✗ Error al cargar vista de Citas: " + e.getMessage());
            mostrarAlerta("Error", "La vista de Citas aún no está disponible", Alert.AlertType.INFORMATION);
        }
    }
    
    @FXML
    private void cerrarSesion() {
        System.out.println("Cerrando sesión...");
        try {
            MainApp.cambiarEscena("hello-view.fxml", "Login - Gestión Médica", 400, 350);
        } catch (Exception e) {
            System.err.println("✗ Error al volver al login: " + e.getMessage());
        }
    }
    
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

package com.example.gestionmedica.controllers;

import com.example.gestionmedica.MainApp;
import com.example.gestionmedica.dao.UsuarioDAO;
import com.example.gestionmedica.models.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class HelloController {
    
    @FXML
    private TextField usuarioField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    private UsuarioDAO usuarioDAO;
    private static Usuario usuarioActual;

    @FXML
    public void initialize() {
        usuarioDAO = new UsuarioDAO();
        System.out.println("✓ HelloController inicializado correctamente");
    }

    @FXML
    protected void onHelloButtonClick() {
        System.out.println("\n========== INTENTO DE LOGIN ==========");
        System.out.println("Campo usuarioField: " + (usuarioField != null ? "OK" : "NULL"));
        System.out.println("Campo passwordField: " + (passwordField != null ? "OK" : "NULL"));
        
        String username = usuarioField != null ? usuarioField.getText().trim() : "";
        String password = passwordField != null ? passwordField.getText() : "";
        
        System.out.println("Usuario ingresado: '" + username + "' (longitud: " + username.length() + ")");
        System.out.println("Password ingresado: '" + password + "' (longitud: " + password.length() + ")");
        System.out.println("=====================================\n");
        
        // Validar que no estén vacíos
        if (username.isEmpty() || password.isEmpty()) {
            System.err.println("✗ Campos vacíos detectados");
            mostrarAlerta("Error", "Por favor, ingresa usuario y contraseña", Alert.AlertType.WARNING);
            return;
        }
        
        // Validar credenciales
        System.out.println("Validando credenciales en la base de datos...");
        Usuario usuario = usuarioDAO.validarCredenciales(username, password);
        
        if (usuario != null) {
            // Login exitoso
            usuarioActual = usuario;
            System.out.println("✓ Login exitoso: " + usuario.getNombreCompleto() + " (" + usuario.getRol() + ")");
            
            mostrarAlerta("Bienvenido", "Hola " + usuario.getNombreCompleto(), Alert.AlertType.INFORMATION);
            
            // Redirigir al Dashboard
            try {
                MainApp.cambiarEscena("Dashboard.fxml", "Dashboard - Sistema Gestión Médica", 1000, 700);
            } catch (Exception e) {
                System.err.println("✗ Error al cargar Dashboard: " + e.getMessage());
                mostrarAlerta("Error", "No se pudo cargar el Dashboard", Alert.AlertType.ERROR);
            }
            
        } else {
            // Login fallido
            System.out.println("✗ Login fallido para usuario: " + username);
            mostrarAlerta("Error de autenticación", "Usuario o contraseña incorrectos", Alert.AlertType.ERROR);
            passwordField.clear();
        }
    }
    
<<<<<<< HEAD
    /**
     * Muestra una alerta al usuario
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    /**
     * Obtiene el usuario que inició sesión
     */
    public static Usuario getUsuarioActual() {
        return usuarioActual;
=======
    @FXML
    public void initialize() {
        System.out.println("✓ HelloController inicializado correctamente");
    private Label lblMensaje;

    private LoginDAO loginDAO = new LoginDAO();

    @FXML
    private void onHelloButtonClick(ActionEvent event) {
        String username = txtUsuario.getText().trim();
        String password = txtPassword.getText();

        // Validación básica
        if (username.isEmpty() || password.isEmpty()) {
            lblMensaje.setText("⚠️ Completa usuario y contraseña");
            lblMensaje.setStyle("-fx-text-fill: orange;");
            return;
        }

        // Intentar login
        Usuario usuario = loginDAO.login(username, password);

        if (usuario != null && usuario.isActivo()) {
            lblMensaje.setText("✅ Login correcto!");
            lblMensaje.setStyle("-fx-text-fill: green;");

            abrirDashboard(usuario);
        } else {
            lblMensaje.setText("❌ Usuario o contraseña incorrectos");
            lblMensaje.setStyle("-fx-text-fill: red;");
            txtPassword.clear();
        }
    }

    private void abrirDashboard(Usuario usuario) {
        try {
            FXMLLoader loader;

            if ("admin".equals(usuario.getRol())) {
                // Dashboard Admin
                loader = new FXMLLoader(getClass().getResource("/fxml/DashboardAdmin.fxml"));
            } else {
                // Dashboard Médico
                loader = new FXMLLoader(getClass().getResource("/fxml/DashboardMedico.fxml"));
            }

            Parent root = loader.load();
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Hospital MVP - " + usuario.getNombre());
            stage.show();

        } catch (Exception e) {
            lblMensaje.setText("Error al abrir dashboard: " + e.getMessage());
            lblMensaje.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
>>>>>>> d88d124dcca57f01a59333bd5c636eaf0b2d4337
    }
}

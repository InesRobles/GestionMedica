package com.example.gestionmedica.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.gestionmedica.dao.LoginDAO;
import com.example.gestionmedica.models.Usuario;

public class HelloController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
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
    }
}

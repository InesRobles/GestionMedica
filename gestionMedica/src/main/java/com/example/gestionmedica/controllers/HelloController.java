package com.example.gestionmedica.controllers;

import javafx.fxml.FXML;
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

    @FXML
    protected void onHelloButtonClick() {
        System.out.println("Botón de login presionado");
        System.out.println("Usuario: " + (usuarioField != null ? usuarioField.getText() : "null"));
        System.out.println("Contraseña: " + (passwordField != null ? passwordField.getText() : "null"));
        
        // Aquí podrías validar credenciales y cambiar de escena
        // Por ahora solo muestra un mensaje
    }
    
    @FXML
    public void initialize() {
        System.out.println("✓ HelloController inicializado correctamente");
    }
}

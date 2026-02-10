package com.example.gestionmedica;

import javafx.application.Application;

/**
 * Clase Launcher alternativa para ejecutar la aplicación
 * Útil para evitar problemas con módulos de JavaFX
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}

module com.example.gestionmedica {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.gestionmedica to javafx.fxml;
    opens com.example.gestionmedica.controllers to javafx.fxml;
    
    exports com.example.gestionmedica;
    exports com.example.gestionmedica.controllers;
    exports com.example.gestionmedica.models;
    exports com.example.gestionmedica.dao;
    exports com.example.gestionmedica.utils;
}
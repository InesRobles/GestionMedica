module com.example.gestionmedica {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gestionmedica to javafx.fxml;
    exports com.example.gestionmedica;
}
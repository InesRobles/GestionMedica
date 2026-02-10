package com.example.gestionmedica.controllers;

import com.example.gestionmedica.MainApp;
import com.example.gestionmedica.dao.MedicoDAO;
import com.example.gestionmedica.models.Medico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Controlador para la gestión de médicos
 */
public class DoctorController {

    @FXML private TableView<Medico> tablaMedicos;
    @FXML private TableColumn<Medico, Integer> colId;
    @FXML private TableColumn<Medico, String> colNombre;
    @FXML private TableColumn<Medico, String> colApellidos;
    @FXML private TableColumn<Medico, String> colEspecialidad;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEspecialidad;

    private MedicoDAO medicoDAO;
    private ObservableList<Medico> listaMedicos;

    @FXML
    public void initialize() {
        medicoDAO = new MedicoDAO();
        System.out.println("✓ DoctorController inicializado");

        // Configurar columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("idMedico"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));

        // Cargar datos
        cargarMedicos();

        // Listener para selección en la tabla
        tablaMedicos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    cargarMedicoEnFormulario(newSelection);
                }
            }
        );
    }

    private void cargarMedicos() {
        try {
            List<Medico> medicos = medicoDAO.obtenerTodos();
            listaMedicos = FXCollections.observableArrayList(medicos);
            tablaMedicos.setItems(listaMedicos);
        } catch (Exception e) {
            System.err.println("Error al cargar médicos: " + e.getMessage());
        }
    }

    private void cargarMedicoEnFormulario(Medico medico) {
        txtNombre.setText(medico.getNombre());
        txtApellidos.setText(medico.getApellidos());
        txtEspecialidad.setText(medico.getEspecialidad());
    }

    @FXML
    private void agregarMedico() {
        if (validarCampos()) {
            Medico medico = new Medico();
            medico.setNombre(txtNombre.getText().trim());
            medico.setApellidos(txtApellidos.getText().trim());
            medico.setEspecialidad(txtEspecialidad.getText().trim());
            if (medicoDAO.insertar(medico)) {
                cargarMedicos();
                limpiarCampos();
                mostrarAlerta("Éxito", "Médico agregado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo agregar el médico", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void actualizarMedico() {
        Medico seleccionado = tablaMedicos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un médico de la tabla", Alert.AlertType.WARNING);
            return;
        }
        if (validarCampos()) {
            seleccionado.setNombre(txtNombre.getText().trim());
            seleccionado.setApellidos(txtApellidos.getText().trim());
            seleccionado.setEspecialidad(txtEspecialidad.getText().trim());
            if (medicoDAO.actualizar(seleccionado)) {
                cargarMedicos();
                limpiarCampos();
                mostrarAlerta("Éxito", "Médico actualizado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el médico", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void eliminarMedico() {
        Medico seleccionado = tablaMedicos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un médico de la tabla", Alert.AlertType.WARNING);
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar al " + seleccionado.getNombreCompleto() + "?");
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (medicoDAO.eliminar(seleccionado.getIdMedico())) {
                    cargarMedicos();
                    limpiarCampos();
                    mostrarAlerta("Éxito", "Médico eliminado", Alert.AlertType.INFORMATION);
                }
            }
        });
    }

    @FXML
    private void limpiarCampos() {
        txtNombre.clear();
        txtApellidos.clear();
        txtEspecialidad.clear();
        tablaMedicos.getSelectionModel().clearSelection();
    }

    @FXML
    private void volverAlDashboard() {
        try {
            MainApp.cambiarEscena("Dashboard.fxml", "Dashboard - Sistema Gestión Médica", 1000, 700);
        } catch (Exception e) {
            System.err.println("Error al volver al dashboard: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() || txtApellidos.getText().trim().isEmpty()
                || txtEspecialidad.getText().trim().isEmpty()) {
            mostrarAlerta("Aviso", "Todos los campos son obligatorios", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

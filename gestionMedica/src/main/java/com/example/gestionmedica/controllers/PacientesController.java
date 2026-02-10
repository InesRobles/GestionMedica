package com.example.gestionmedica.controllers;

import com.example.gestionmedica.MainApp;
import com.example.gestionmedica.dao.PacienteDAO;
import com.example.gestionmedica.models.Paciente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

/**
 * Controlador para la gestión de pacientes
 */
public class PacientesController {

    @FXML private TableView<Paciente> tablaPacientes;
    @FXML private TableColumn<Paciente, Integer> colId;
    @FXML private TableColumn<Paciente, String> colNombre;
    @FXML private TableColumn<Paciente, String> colApellidos;
    @FXML private TableColumn<Paciente, String> colDni;
    @FXML private TableColumn<Paciente, String> colEmail;
    @FXML private TableColumn<Paciente, String> colTelefono;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtDni;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono;

    private PacienteDAO pacienteDAO;
    private ObservableList<Paciente> listaPacientes;

    @FXML
    public void initialize() {
        pacienteDAO = new PacienteDAO();
        System.out.println("✓ PacientesController inicializado");

        // Configurar columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("idPaciente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        // Cargar datos
        cargarPacientes();

        // Listener para selección en la tabla
        tablaPacientes.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    cargarPacienteEnFormulario(newSelection);
                }
            }
        );
    }

    private void cargarPacientes() {
        try {
            List<Paciente> pacientes = pacienteDAO.obtenerTodos();
            listaPacientes = FXCollections.observableArrayList(pacientes);
            tablaPacientes.setItems(listaPacientes);
        } catch (Exception e) {
            System.err.println("Error al cargar pacientes: " + e.getMessage());
        }
    }

    private void cargarPacienteEnFormulario(Paciente paciente) {
        txtNombre.setText(paciente.getNombre());
        txtApellidos.setText(paciente.getApellidos());
        txtDni.setText(paciente.getDni());
        txtEmail.setText(paciente.getEmail());
        txtTelefono.setText(paciente.getTelefono());
    }

    @FXML
    private void agregarPaciente() {
        if (validarCampos()) {
            Paciente paciente = crearPacienteDesdeFormulario();
            if (pacienteDAO.insertar(paciente)) {
                cargarPacientes();
                limpiarCampos();
                mostrarAlerta("Éxito", "Paciente agregado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo agregar el paciente", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void actualizarPaciente() {
        Paciente seleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un paciente de la tabla", Alert.AlertType.WARNING);
            return;
        }
        if (validarCampos()) {
            seleccionado.setNombre(txtNombre.getText().trim());
            seleccionado.setApellidos(txtApellidos.getText().trim());
            seleccionado.setDni(txtDni.getText().trim());
            seleccionado.setEmail(txtEmail.getText().trim());
            seleccionado.setTelefono(txtTelefono.getText().trim());
            if (pacienteDAO.actualizar(seleccionado)) {
                cargarPacientes();
                limpiarCampos();
                mostrarAlerta("Éxito", "Paciente actualizado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el paciente", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void eliminarPaciente() {
        Paciente seleccionado = tablaPacientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Aviso", "Selecciona un paciente de la tabla", Alert.AlertType.WARNING);
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar paciente " + seleccionado.getNombre() + "?");
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (pacienteDAO.eliminar(seleccionado.getIdPaciente())) {
                    cargarPacientes();
                    limpiarCampos();
                    mostrarAlerta("Éxito", "Paciente eliminado", Alert.AlertType.INFORMATION);
                }
            }
        });
    }

    @FXML
    private void limpiarCampos() {
        txtNombre.clear();
        txtApellidos.clear();
        txtDni.clear();
        txtEmail.clear();
        txtTelefono.clear();
        tablaPacientes.getSelectionModel().clearSelection();
    }

    @FXML
    private void volverAlDashboard() {
        try {
            MainApp.cambiarEscena("Dashboard.fxml", "Dashboard - Sistema Gestión Médica", 1000, 700);
        } catch (Exception e) {
            System.err.println("Error al volver al dashboard: " + e.getMessage());
        }
    }

    private Paciente crearPacienteDesdeFormulario() {
        Paciente p = new Paciente();
        p.setNombre(txtNombre.getText().trim());
        p.setApellidos(txtApellidos.getText().trim());
        p.setDni(txtDni.getText().trim());
        p.setEmail(txtEmail.getText().trim());
        p.setTelefono(txtTelefono.getText().trim());
        return p;
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() || txtApellidos.getText().trim().isEmpty()) {
            mostrarAlerta("Aviso", "Nombre y Apellidos son obligatorios", Alert.AlertType.WARNING);
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

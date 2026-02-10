package com.example.gestionmedica.controllers;

import com.example.gestionmedica.MainApp;
import com.example.gestionmedica.dao.CitaDAO;
import com.example.gestionmedica.models.Cita;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controlador para la gestión de citas médicas
 */
public class CitasController {

    @FXML private TableView<Cita> tablaCitas;
    @FXML private TableColumn<Cita, Integer> colId;
    @FXML private TableColumn<Cita, String> colPaciente;
    @FXML private TableColumn<Cita, String> colMedico;
    @FXML private TableColumn<Cita, LocalDate> colFecha;
    @FXML private TableColumn<Cita, LocalTime> colHora;
    @FXML private TableColumn<Cita, String> colMotivo;
    @FXML private TableColumn<Cita, String> colEstado;

    @FXML private TextField txtPaciente;
    @FXML private TextField txtMedico;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;
    @FXML private TextField txtMotivo;

    private CitaDAO citaDAO;
    private ObservableList<Cita> listaCitas;

    @FXML
    public void initialize() {
        citaDAO = new CitaDAO();
        System.out.println("✓ CitasController inicializado");

        // Configurar columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("nombrePaciente"));
        colMedico.setCellValueFactory(new PropertyValueFactory<>("nombreMedico"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCita"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("horaCita"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Cargar datos
        cargarCitas();

        // Listener para selección en la tabla
        tablaCitas.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    cargarCitaEnFormulario(newSelection);
                }
            }
        );
    }

    private void cargarCitas() {
        try {
            List<Cita> citas = citaDAO.obtenerTodas();
            listaCitas = FXCollections.observableArrayList(citas);
            tablaCitas.setItems(listaCitas);
        } catch (Exception e) {
            System.err.println("Error al cargar citas: " + e.getMessage());
        }
    }

    private void cargarCitaEnFormulario(Cita cita) {
        txtPaciente.setText(String.valueOf(cita.getIdPaciente()));
        txtMedico.setText(String.valueOf(cita.getIdMedico()));
        dpFecha.setValue(cita.getFechaCita());
        txtHora.setText(cita.getHoraCita() != null ? cita.getHoraCita().toString() : "");
        txtMotivo.setText(cita.getMotivo());
    }

    @FXML
    private void agregarCita() {
        if (validarCampos()) {
            try {
                Cita cita = crearCitaDesdeFormulario();
                if (citaDAO.insertar(cita)) {
                    cargarCitas();
                    limpiarCampos();
                    mostrarAlerta("Éxito", "Cita programada correctamente", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Error", "No se pudo programar la cita", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "ID de paciente y médico deben ser números", Alert.AlertType.ERROR);
            } catch (DateTimeParseException e) {
                mostrarAlerta("Error", "Formato de hora inválido. Usa HH:MM (ej: 09:30)", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void actualizarCita() {
        Cita seleccionada = tablaCitas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona una cita de la tabla", Alert.AlertType.WARNING);
            return;
        }
        if (validarCampos()) {
            try {
                seleccionada.setIdPaciente(Integer.parseInt(txtPaciente.getText().trim()));
                seleccionada.setIdMedico(Integer.parseInt(txtMedico.getText().trim()));
                seleccionada.setFechaCita(dpFecha.getValue());
                seleccionada.setHoraCita(LocalTime.parse(txtHora.getText().trim()));
                seleccionada.setMotivo(txtMotivo.getText().trim());
                if (citaDAO.actualizar(seleccionada)) {
                    cargarCitas();
                    limpiarCampos();
                    mostrarAlerta("Éxito", "Cita actualizada correctamente", Alert.AlertType.INFORMATION);
                } else {
                    mostrarAlerta("Error", "No se pudo actualizar la cita", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "ID de paciente y médico deben ser números", Alert.AlertType.ERROR);
            } catch (DateTimeParseException e) {
                mostrarAlerta("Error", "Formato de hora inválido. Usa HH:MM", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void eliminarCita() {
        Cita seleccionada = tablaCitas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Aviso", "Selecciona una cita de la tabla", Alert.AlertType.WARNING);
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar esta cita?");
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (citaDAO.eliminar(seleccionada.getIdCita())) {
                    cargarCitas();
                    limpiarCampos();
                    mostrarAlerta("Éxito", "Cita eliminada", Alert.AlertType.INFORMATION);
                }
            }
        });
    }

    @FXML
    private void limpiarCampos() {
        txtPaciente.clear();
        txtMedico.clear();
        dpFecha.setValue(null);
        txtHora.clear();
        txtMotivo.clear();
        tablaCitas.getSelectionModel().clearSelection();
    }

    @FXML
    private void volverAlDashboard() {
        try {
            MainApp.cambiarEscena("Dashboard.fxml", "Dashboard - Sistema Gestión Médica", 1000, 700);
        } catch (Exception e) {
            System.err.println("Error al volver al dashboard: " + e.getMessage());
        }
    }

    private Cita crearCitaDesdeFormulario() {
        Cita cita = new Cita();
        cita.setIdPaciente(Integer.parseInt(txtPaciente.getText().trim()));
        cita.setIdMedico(Integer.parseInt(txtMedico.getText().trim()));
        cita.setFechaCita(dpFecha.getValue());
        cita.setHoraCita(LocalTime.parse(txtHora.getText().trim()));
        cita.setMotivo(txtMotivo.getText().trim());
        cita.setEstado(Cita.EstadoCita.PROGRAMADA);
        return cita;
    }

    private boolean validarCampos() {
        if (txtPaciente.getText().trim().isEmpty() || txtMedico.getText().trim().isEmpty()
                || dpFecha.getValue() == null || txtHora.getText().trim().isEmpty()) {
            mostrarAlerta("Aviso", "Paciente, Médico, Fecha y Hora son obligatorios", Alert.AlertType.WARNING);
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

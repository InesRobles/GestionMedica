package com.example.gestionmedica.models;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase que representa una Cita médica en el sistema
 * Mapea con la tabla 'citas' de la base de datos
 */
public class Cita {
    
    private int idCita;
    private int idPaciente;
    private int idMedico;
    private LocalDate fechaCita;
    private LocalTime horaCita;
    private String motivo;
    private EstadoCita estado;
    
    // Datos adicionales para mostrar en la UI (no están en la BD)
    private String nombrePaciente;
    private String nombreMedico;

    // Enum para los estados de la cita
    public enum EstadoCita {
        PROGRAMADA("programada"),
        CONFIRMADA("confirmada"),
        COMPLETADA("completada"),
        CANCELADA("cancelada");

        private final String valor;

        EstadoCita(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public static EstadoCita fromString(String texto) {
            for (EstadoCita estado : EstadoCita.values()) {
                if (estado.valor.equalsIgnoreCase(texto)) {
                    return estado;
                }
            }
            return PROGRAMADA; // Por defecto
        }
    }

    // Constructor vacío
    public Cita() {
        this.estado = EstadoCita.PROGRAMADA;
    }

    // Constructor completo
    public Cita(int idCita, int idPaciente, int idMedico, LocalDate fechaCita, 
                LocalTime horaCita, String motivo, EstadoCita estado) {
        this.idCita = idCita;
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.motivo = motivo;
        this.estado = estado;
    }

    // Constructor sin ID (para inserciones)
    public Cita(int idPaciente, int idMedico, LocalDate fechaCita, 
                LocalTime horaCita, String motivo, EstadoCita estado) {
        this.idPaciente = idPaciente;
        this.idMedico = idMedico;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.motivo = motivo;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public LocalDate getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    public LocalTime getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(LocalTime horaCita) {
        this.horaCita = horaCita;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }

    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    @Override
    public String toString() {
        return "Cita{" +
                "idCita=" + idCita +
                ", fechaCita=" + fechaCita +
                ", horaCita=" + horaCita +
                ", paciente='" + nombrePaciente + '\'' +
                ", medico='" + nombreMedico + '\'' +
                ", estado=" + estado +
                '}';
    }
}

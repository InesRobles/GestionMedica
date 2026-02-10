package com.example.gestionmedica.models;

/**
 * Clase que representa un Médico en el sistema
 * Mapea con la tabla 'medicos' de la base de datos
 */
public class Medico {
    
    private int idMedico;
    private String nombre;
    private String apellidos;
    private String especialidad;

    // Constructor vacío
    public Medico() {
    }

    // Constructor completo
    public Medico(int idMedico, String nombre, String apellidos, String especialidad) {
        this.idMedico = idMedico;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
    }

    // Constructor sin ID (para inserciones)
    public Medico(String nombre, String apellidos, String especialidad) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
    }

    // Getters y Setters
    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNombreCompleto() {
        return "Dr./Dra. " + nombre + " " + apellidos;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " - " + especialidad;
    }
}

package com.example.gestionmedica.models;

public class Usuario {
    private int id;
    private String username, password, rol, nombre;
    private boolean activo;

    // Constructor, getters y setters
    public Usuario(String username, String rol, String nombre) {
        this.username = username;
        this.rol = rol;
        this.nombre = nombre;
    }

    // getters/setters...
    public String getRol() { return rol; }
    public String getNombre() { return nombre; }
    public boolean isActivo() { return activo; }

    public void setActivo(boolean activo) {
    }
}

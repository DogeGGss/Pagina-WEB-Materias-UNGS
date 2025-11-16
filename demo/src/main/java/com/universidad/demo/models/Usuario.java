package com.universidad.demo.models;

import java.util.List;

public class Usuario {
    private String username;
    private String nombreCompleto;
    private String avatarUrl;
    private String rol;
    private List<String> materias; // Materias asociadas al usuario

    // Constructor
    public Usuario(String username, String nombreCompleto, String avatarUrl, String rol, List<String> materias) {
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.avatarUrl = avatarUrl;
        this.rol = rol;
        this.materias = materias;
    }

    // Getters
    public String getUsername() { return username; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getRol() { return rol; }
    public List<String> getMaterias() { return materias; }
}
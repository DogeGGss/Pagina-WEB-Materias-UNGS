package com.universidad.demo.models;

import jakarta.persistence.*;

@Entity
public class MateriaAprobada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // Usuario que aprobó la materia
    private String materiaCodigo; // Código de la materia aprobada

    public MateriaAprobada() {}

    public MateriaAprobada(String username, String materiaCodigo) {
        this.username = username;
        this.materiaCodigo = materiaCodigo;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMateriaCodigo() {
        return materiaCodigo;
    }

    public void setMateriaCodigo(String materiaCodigo) {
        this.materiaCodigo = materiaCodigo;
    }
}
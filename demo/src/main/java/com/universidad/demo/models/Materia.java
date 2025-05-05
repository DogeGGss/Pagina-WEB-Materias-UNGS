package com.universidad.demo.models;

import java.util.List;
import java.util.Map;

public class Materia {
    private String codigo;
    private String nombre;
    private String regimen;
    private int horasSemanales;
    private int horasTotales;
    private boolean aprobada;
    private List<String> correlativasCodigos;

    // Constructor completo
    public Materia(String codigo, String nombre, String regimen, 
                  int horasSemanales, int horasTotales,
                  List<String> correlativasCodigos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.regimen = regimen;
        this.horasSemanales = horasSemanales;
        this.horasTotales = horasTotales;
        this.correlativasCodigos = correlativasCodigos;
        this.aprobada = false; // Por defecto no aprobada
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRegimen() {
        return regimen;
    }

    public int getHorasSemanales() {
        return horasSemanales;
    }

    public int getHorasTotales() {
        return horasTotales;
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public List<String> getCorrelativasCodigos() {
        return correlativasCodigos;
    }

    // Método para verificar si se puede cursar
    public boolean puedeCursar(Map<String, Materia> todasLasMaterias, List<String> materiasAprobadas) {
        return correlativasCodigos.stream()
               .allMatch(materiasAprobadas::contains);
    }

    // Método para marcar como aprobada
    public void marcarComoAprobada() {
        this.aprobada = true;
    }
}
package com.universidad.demo.repositories;

import com.universidad.demo.models.MateriaAprobada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MateriaAprobadaRepository extends JpaRepository<MateriaAprobada, Long> {
    List<MateriaAprobada> findByUsername(String username);
    void deleteByUsername(String username);
}
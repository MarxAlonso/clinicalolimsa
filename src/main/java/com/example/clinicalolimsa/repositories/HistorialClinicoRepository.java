package com.example.clinicalolimsa.repositories;

import com.example.clinicalolimsa.models.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Integer> {
    List<HistorialClinico> findByPacienteId(Integer pacienteId);
}

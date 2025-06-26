package com.example.clinicalolimsa.repositories;

import com.example.clinicalolimsa.models.Compra;
import com.example.clinicalolimsa.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Integer> {
    List<Compra> findByPaciente(Paciente paciente);
}

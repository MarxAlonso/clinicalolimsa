package com.example.clinicalolimsa.repositories;

import com.example.clinicalolimsa.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente,Integer> {

    Paciente findByEmail(String email);
    List<Paciente> findByRole(String role);
    boolean existsByEmail(String email);

    List<Paciente> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDocumentoIdentidad(String firstName, String lastName, String documentoIdentidad);
}

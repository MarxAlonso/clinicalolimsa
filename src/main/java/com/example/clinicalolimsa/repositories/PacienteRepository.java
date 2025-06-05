package com.example.clinicalolimsa.repositories;

import com.example.clinicalolimsa.models.AppUser;
import com.example.clinicalolimsa.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByUser(AppUser user);
}


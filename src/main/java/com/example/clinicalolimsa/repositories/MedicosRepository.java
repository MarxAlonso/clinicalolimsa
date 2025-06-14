package com.example.clinicalolimsa.repositories;

import com.example.clinicalolimsa.models.Medicos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicosRepository extends JpaRepository<Medicos, Integer> {

    Medicos findByCorreo(String correo);

    List<Medicos> findByEspecialidad(String especialidad);

    boolean existsByCorreo(String correo);
}

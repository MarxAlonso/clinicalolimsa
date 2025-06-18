package com.example.clinicalolimsa.repositories;
import com.example.clinicalolimsa.models.Citas;
import com.example.clinicalolimsa.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CitasRepository extends JpaRepository<Citas, Integer> {

    // Buscar todas las citas de un paciente
    List<Citas> findByPacienteId(Integer pacienteId);

    // Buscar todas las citas de un médico
    List<Citas> findByMedicoId(Integer medicoId);

    // Buscar citas por fecha
    List<Citas> findByFecha(Date fecha);

    // Buscar citas por médico y fecha
    List<Citas> findByMedicoIdAndFecha(Integer medicoId, Date fecha);

    // Buscar citas por estado (Ej: "Programada", "Cancelada", "Completada")
    List<Citas> findByEstado(String estado);
    List<Citas> findByPaciente(Paciente paciente);

}

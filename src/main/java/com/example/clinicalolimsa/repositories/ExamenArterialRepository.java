package com.example.clinicalolimsa.repositories;

import com.example.clinicalolimsa.models.ExamenArterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenArterialRepository extends JpaRepository<ExamenArterial, Integer> {
    List<ExamenArterial> findByCita_Medico_Id(Integer medicoId);
}


package com.example.clinicalolimsa.repositories;

import com.example.clinicalolimsa.models.ExamenSangre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenSangreRepository extends JpaRepository<ExamenSangre, Integer> {
    List<ExamenSangre> findByCita_Medico_Id(Integer medicoId);
}

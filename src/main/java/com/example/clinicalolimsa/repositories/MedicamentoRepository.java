package com.example.clinicalolimsa.repositories;

import com.example.clinicalolimsa.models.Medicamentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamentos, Integer> {
}
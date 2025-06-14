package com.example.clinicalolimsa.repositories;

import com.example.clinicalolimsa.models.Medicamentos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentosRepository  extends JpaRepository<Medicamentos, Integer> {
}

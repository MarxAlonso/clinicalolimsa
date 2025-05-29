package com.example.clinicalolimsa.models;

import java.util.List;

public class EstadisticasDTO {
    private long totalPacientes;
    private long totalMedicos;
    private long totalMedicamentos;

    private List<Long> pacientesPorSemana;
    private List<Long> medicamentosPorSemana;

    // Constructor, getters y setters
    public EstadisticasDTO(){}
    public EstadisticasDTO(long totalPacientes, long totalMedicos, long totalMedicamentos, List<Long> pacientesPorSemana, List<Long> medicamentosPorSemana) {
        this.totalPacientes = totalPacientes;
        this.totalMedicos = totalMedicos;
        this.totalMedicamentos = totalMedicamentos;
        this.pacientesPorSemana = pacientesPorSemana;
        this.medicamentosPorSemana = medicamentosPorSemana;
    }

    public long getTotalPacientes() {
        return totalPacientes;
    }

    public void setTotalPacientes(long totalPacientes) {
        this.totalPacientes = totalPacientes;
    }

    public long getTotalMedicos() {
        return totalMedicos;
    }

    public void setTotalMedicos(long totalMedicos) {
        this.totalMedicos = totalMedicos;
    }

    public long getTotalMedicamentos() {
        return totalMedicamentos;
    }

    public void setTotalMedicamentos(long totalMedicamentos) {
        this.totalMedicamentos = totalMedicamentos;
    }

    public List<Long> getPacientesPorSemana() {
        return pacientesPorSemana;
    }

    public void setPacientesPorSemana(List<Long> pacientesPorSemana) {
        this.pacientesPorSemana = pacientesPorSemana;
    }

    public List<Long> getMedicamentosPorSemana() {
        return medicamentosPorSemana;
    }

    public void setMedicamentosPorSemana(List<Long> medicamentosPorSemana) {
        this.medicamentosPorSemana = medicamentosPorSemana;
    }
}
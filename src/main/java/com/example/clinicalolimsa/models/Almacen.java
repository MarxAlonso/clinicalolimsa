package com.example.clinicalolimsa.models;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "almacen")
public class Almacen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ubicacion;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;

    @ManyToOne
    @JoinColumn(name = "medicamento_id")
    private Medicamentos medicamento;

    // Getters y Setters
    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getUbicacion() { return ubicacion; }

    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public Date getFechaIngreso() { return fechaIngreso; }

    public void setFechaIngreso(Date fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public Medicamentos getMedicamento() { return medicamento; }

    public void setMedicamento(Medicamentos medicamento) { this.medicamento = medicamento; }
}
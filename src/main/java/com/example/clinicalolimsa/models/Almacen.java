package com.example.clinicalolimsa.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "almacen")
public class Almacen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "La ubicación no puede estar vacía")
    @Size(min = 5, max = 100, message = "La ubicación debe tener entre 2 y 100 caracteres")
    private String ubicacion;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;

    @NotNull(message = "Debe seleccionar un medicamento")
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
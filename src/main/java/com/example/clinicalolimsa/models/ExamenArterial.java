package com.example.clinicalolimsa.models;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "examen_arterial")
public class ExamenArterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación con la cita
    @ManyToOne
    @JoinColumn(name = "cita_id", nullable = false)
    private Citas cita;

    private double presionSistolica;
    private double presionDiastolica;
    private String observaciones;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro = new Date();

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Citas getCita() {
        return cita;
    }

    public void setCita(Citas cita) {
        this.cita = cita;
    }

    public double getPresionSistolica() {
        return presionSistolica;
    }

    public void setPresionSistolica(double presionSistolica) {
        this.presionSistolica = presionSistolica;
    }

    public double getPresionDiastolica() {
        return presionDiastolica;
    }

    public void setPresionDiastolica(double presionDiastolica) {
        this.presionDiastolica = presionDiastolica;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}


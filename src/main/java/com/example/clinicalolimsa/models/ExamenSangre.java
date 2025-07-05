package com.example.clinicalolimsa.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "examen_sangre")
public class ExamenSangre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cita_id", nullable = false)
    private Citas cita;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro = new Date();

    private Double hemoglobina;
    private Double glucosa;
    private Double colesterol;

    @Lob
    private String observaciones;

    // Getters y setters

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

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Double getHemoglobina() {
        return hemoglobina;
    }

    public void setHemoglobina(Double hemoglobina) {
        this.hemoglobina = hemoglobina;
    }

    public Double getGlucosa() {
        return glucosa;
    }

    public void setGlucosa(Double glucosa) {
        this.glucosa = glucosa;
    }

    public Double getColesterol() {
        return colesterol;
    }

    public void setColesterol(Double colesterol) {
        this.colesterol = colesterol;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}


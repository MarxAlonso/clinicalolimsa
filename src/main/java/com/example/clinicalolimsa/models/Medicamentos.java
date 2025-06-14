package com.example.clinicalolimsa.models;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "medicamentos")
public class Medicamentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String descripcion;

    private int cantidad; // número de unidades del medicamento en inventario
    private double precio;

    private double cantidadMedida; // Ej: 250
    private String unidadMedida;   // Ej: "ml", "g", "tabletas"

    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    private String imagen; // nombre del archivo

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private TipoDeMedicamento tipo;


    // Getters y Setters
    public Integer getId()              {       return id;    }
    public void setId(Integer id)       {       this.id = id;    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public TipoDeMedicamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoDeMedicamento tipo) {
        this.tipo = tipo;
    }

    public double getCantidadMedida() {
        return cantidadMedida;
    }

    public void setCantidadMedida(double cantidadMedida) {
        this.cantidadMedida = cantidadMedida;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}

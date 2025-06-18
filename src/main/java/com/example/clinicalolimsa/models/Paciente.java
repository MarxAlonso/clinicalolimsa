package com.example.clinicalolimsa.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="pacientes")
public class Paciente {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    @Column(unique=true, nullable=false)
    private String email;
    private String phone;
    private String address;
    private String password;
    private String role;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // Nuevos campos sugeridos
    private String sexo;                     // Masculino / Femenino / Otro
    private LocalDate fechaNacimiento;
    private String tipoSangre;               // Ej: A+, O-, etc.
    private String documentoIdentidad;       // DNI o Pasaporte
    private String nacionalidad;

    private String estadoCivil;             // Soltero, Casado, etc.
    private String ocupacion;

    // Contacto de emergencia
    private String contactoEmergenciaNombre;
    private String contactoEmergenciaTelefono;
    private String contactoEmergenciaRelacion;

    private String alergias;                // Coma separado: "Penicilina, Mariscos"
    private String enfermedadesPrevias;     // Coma separado: "Diabetes, Hipertensión"
    private String medicamentosActuales;    // Coma separado
    //getter y setter
    public Integer getId()              {       return id;    }
    public void setId(Integer id)       {       this.id = id;    }
    public String getFirstName()    {       return firstName;    }
    public void setFirstName(String firstName) {       this.firstName = firstName;    }
    public String getLastName()     {       return lastName;    }
    public void setLastName(String lastName) {       this.lastName = lastName;    }
    public String getEmail()        {       return email;    }
    public void setEmail(String email) {        this.email = email;    }
    public String getPhone() {        return phone;    }
    public void setPhone(String phone) {        this.phone = phone;    }
    public String getAddress() {        return address;    }
    public void setAddress(String address) {        this.address = address;    }
    public String getPassword() {        return password;    }
    public void setPassword(String password) {        this.password = password;    }
    public String getRole() {        return role;    }
    public void setRole(String role) {        this.role = role;    }
    public Date getCreatedAt() {        return createdAt;    }
    public void setCreatedAt(Date createdAt) {        this.createdAt = createdAt;    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getContactoEmergenciaNombre() {
        return contactoEmergenciaNombre;
    }

    public void setContactoEmergenciaNombre(String contactoEmergenciaNombre) {
        this.contactoEmergenciaNombre = contactoEmergenciaNombre;
    }

    public String getContactoEmergenciaTelefono() {
        return contactoEmergenciaTelefono;
    }

    public void setContactoEmergenciaTelefono(String contactoEmergenciaTelefono) {
        this.contactoEmergenciaTelefono = contactoEmergenciaTelefono;
    }

    public String getContactoEmergenciaRelacion() {
        return contactoEmergenciaRelacion;
    }

    public void setContactoEmergenciaRelacion(String contactoEmergenciaRelacion) {
        this.contactoEmergenciaRelacion = contactoEmergenciaRelacion;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getEnfermedadesPrevias() {
        return enfermedadesPrevias;
    }

    public void setEnfermedadesPrevias(String enfermedadesPrevias) {
        this.enfermedadesPrevias = enfermedadesPrevias;
    }

    public String getMedicamentosActuales() {
        return medicamentosActuales;
    }

    public void setMedicamentosActuales(String medicamentosActuales) {
        this.medicamentosActuales = medicamentosActuales;
    }
}

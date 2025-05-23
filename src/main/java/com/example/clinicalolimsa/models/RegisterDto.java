package com.example.clinicalolimsa.models;

import jakarta.validation.constraints.*;
public class RegisterDto {
    @NotEmpty(message="El nombre no puede ser vacío")
    private String firstName;
    @NotEmpty(message="El apellido no puede ser vacío")
    private String lastName;
    @NotEmpty(message="El email no puede ser vacío")
    @Email (message="El mail debe de tener un @")
    private String email;
    private String phone;
    private String address;
    @Size(min=6, message="El tamaño minimo de caracteres es 6")
    private String password;
    private String confirmPassword;

    //getter y setter
    public String getFirstName() {     return firstName;    }
    public void setFirstName(String firstName) {    this.firstName = firstName;    }
    public String getLastName() {      return lastName;    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}//fin de la clase

package com.example.clinicalolimsa.security;

import com.example.clinicalolimsa.models.Paciente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class PacienteUserDetails implements UserDetails {

    private Paciente paciente;

    public PacienteUserDetails(Paciente paciente) {
        this.paciente = paciente;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_pacientes"));
    }

    @Override
    public String getPassword() {
        return paciente.getPassword();
    }

    @Override
    public String getUsername() {
        return paciente.getEmail();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}

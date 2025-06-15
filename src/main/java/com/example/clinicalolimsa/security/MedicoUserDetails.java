package com.example.clinicalolimsa.security;

import com.example.clinicalolimsa.models.Medicos;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MedicoUserDetails implements UserDetails {

    private Medicos medico;

    public MedicoUserDetails(Medicos medico) {
        this.medico = medico;
    }

    public Medicos getMedico() {
        return medico;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_medico"));
    }

    @Override
    public String getPassword() {
        return medico.getContrasena();
    }

    @Override
    public String getUsername() {
        return medico.getCorreo();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}

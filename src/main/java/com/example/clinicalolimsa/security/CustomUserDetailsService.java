package com.example.clinicalolimsa.security;

import com.example.clinicalolimsa.models.AppUser;
import com.example.clinicalolimsa.models.Medicos;
import com.example.clinicalolimsa.repositories.AppUserRepository;
import com.example.clinicalolimsa.repositories.MedicosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private MedicosRepository medicoRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        AppUser user = userRepository.findByEmail(email);
        if (user != null) {
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        }

        Medicos medicos = medicoRepository.findByCorreo(email);
        if (medicos != null) {
            // Listado de cargos válidos para ingresar al sistema
            List<String> especialidadValidos = List.of("pediatria", "cardiologia");

            String especialidad = medicos.getEspecialidad().toLowerCase();

            if (especialidadValidos.contains(especialidad)) {
                return User.builder()
                        .username(medicos.getCorreo())
                        .password(medicos.getContrasena()) // debe estar encriptada
                        .roles("medico") // se mapea como un solo rol para Spring Security
                        .build();
            } else {
                throw new UsernameNotFoundException("El cargo del medico no tiene permisos de acceso.");
            }
        }

        throw new UsernameNotFoundException("Usuario no encontrado");
    }
}

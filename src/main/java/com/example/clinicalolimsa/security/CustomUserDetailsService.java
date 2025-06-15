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

        Medicos medico = medicoRepository.findByCorreo(email);
        if (medico != null) {
            // Validar especialidades válidas
            List<String> especialidadesValidas = List.of("pediatria", "cardiologia");

            if (especialidadesValidas.contains(medico.getEspecialidad().toLowerCase())) {
                return new MedicoUserDetails(medico);
            } else {
                throw new UsernameNotFoundException("El médico no tiene permisos de acceso.");
            }
        }

        throw new UsernameNotFoundException("Usuario no encontrado");
    }
}

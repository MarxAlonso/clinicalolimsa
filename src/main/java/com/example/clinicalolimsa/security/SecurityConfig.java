package com.example.clinicalolimsa.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private AuthenticationSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/img/**", "/webjars/**", "/uploads/**").permitAll()

                        .requestMatchers("/", "/contacto", "/register", "/login", "/logout", "/servicios", "/nosotros").permitAll()

                        .requestMatchers("/pacientes/**").hasRole("pacientes")

                        .requestMatchers("/medicos/**").hasRole("medico")

                        .requestMatchers("/gerente/**").hasRole("gerente")

                        .anyRequest().authenticated()
                )
                // Configura el formulario de login
                .formLogin(form -> form
                        .loginPage("/login") // Especifica la URL personalizada del formulario de login
                        .usernameParameter("email") // Campo del formulario que se usará como nombre de usuario
                        .passwordParameter("password") // Campo del formulario para la contraseña
                        .successHandler(customSuccessHandler) // Handler personalizado para redirigir tras login exitoso
                        .permitAll() // Permite a todos los usuarios acceder al login
                )
                // Configura el logout
                .logout(logout ->
                        logout.logoutSuccessUrl("/").permitAll() // Redirige al inicio después de cerrar sesión
                );

        return http.build(); // Construye y retorna el SecurityFilterChain
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Bean que proporciona un codificador de contraseñas seguro usando BCrypt
        // Se utiliza tanto para guardar como para verificar contraseñas codificadas
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Bean que expone el AuthenticationManager para poder usarlo en otras partes del proyecto (ej: login manual)
        return config.getAuthenticationManager();
    }
}

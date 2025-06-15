package com.example.clinicalolimsa.security;

import com.example.clinicalolimsa.models.AppUser;
import com.example.clinicalolimsa.repositories.AppUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component // Indica que esta clase es un componente de Spring, y será detectada automáticamente como un bean
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    // Esta clase implementa la interfaz AuthenticationSuccessHandler
    // y define el comportamiento personalizado tras un inicio de sesión exitoso

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        // Este método se ejecuta automáticamente cuando el login es exitoso

        String redirectURL = request.getContextPath(); // Obtiene la URL base del contexto de la aplicación, por ejemplo: "/miApp"

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_pacientes"))) {
            redirectURL += "/panelcliente";
        }

        else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_medico"))) {
            redirectURL += "/medicos/medicospanel";
        }
        else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_gerente"))) {
            redirectURL += "/gerente/gerentepanel";
        }
        else {
            redirectURL += "/"; // Si no tiene ningún rol reconocido, redirige a la raíz
        }

        response.sendRedirect(redirectURL); // Envía la redirección al navegador del usuario hacia la URL correspondiente
    }
}

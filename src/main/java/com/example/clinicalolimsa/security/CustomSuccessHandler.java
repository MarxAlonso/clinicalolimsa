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

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AppUserRepository repo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        AppUser user = repo.findByEmail(email);

        String redirectURL = request.getContextPath();

        switch (user.getRole()) {
            case "gerente":
                redirectURL += "/panelgerente";
                break;
            case "paciente":
                redirectURL += "/panelpaciente";
                break;
            case "medico":
                redirectURL += "/panelmedico";
                break;
            default:
                redirectURL += "/";
                break;
        }

        response.sendRedirect(redirectURL);
    }
}

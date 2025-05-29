package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.models.AppUser;
import com.example.clinicalolimsa.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/panelpaciente")
public class PanelPacienteController {

    @Autowired
    private AppUserRepository userRepository;

    @GetMapping("/perfil")
    public String verPerfil(Model model, Principal principal) {
        // Obtener el email del usuario logueado
        String email = principal.getName();
        AppUser user = userRepository.findByEmail(email);
        model.addAttribute("paciente", user);
        return "panelpaciente/perfil"; // ruta de la vista
    }
}

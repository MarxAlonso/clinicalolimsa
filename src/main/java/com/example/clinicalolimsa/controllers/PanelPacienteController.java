package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.models.AppUser;
import com.example.clinicalolimsa.models.Paciente;
import com.example.clinicalolimsa.models.PacienteDto;
import com.example.clinicalolimsa.repositories.AppUserRepository;
import com.example.clinicalolimsa.repositories.PacienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;

@Controller
@RequestMapping("/panelpaciente")
public class PanelPacienteController {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PacienteRepository pacienteRepo;

    // Ver perfil
    @GetMapping("/perfil")
    public String verPerfil(Model model, Principal principal) {
        String email = principal.getName();
        AppUser user = userRepository.findByEmail(email);
        model.addAttribute("paciente", user); // Datos de AppUser

        // Buscar los datos clínicos del paciente
        Paciente datosPaciente = pacienteRepo.findByUser(user);
        model.addAttribute("datosPaciente", datosPaciente); // Puede ser null

        return "panelpaciente/perfil";
    }


    // Mostrar formulario o ver datos del paciente
    @GetMapping("/datos")
    public String mostrarFormularioPaciente(Model model, Principal principal) {
        String email = principal.getName();
        AppUser user = userRepository.findByEmail(email);

        Paciente pacienteExistente = pacienteRepo.findByUser(user);

        if (pacienteExistente != null) {
            model.addAttribute("paciente", pacienteExistente);
            return "panelpaciente/perfil";
        }

        model.addAttribute("pacienteDto", new PacienteDto());
        return "panelpaciente/form_datos_paciente";
    }

    // Guardar datos del paciente
    @PostMapping("/datos")
    public String guardarDatosPaciente(@ModelAttribute PacienteDto pacienteDto, Principal principal) {
        String email = principal.getName();
        AppUser user = userRepository.findByEmail(email);

        Paciente paciente = new Paciente();
        paciente.setUser(user);
        paciente.setDni(pacienteDto.getDni());
        paciente.setGenero(pacienteDto.getGenero());
        paciente.setGrupoSanguineo(pacienteDto.getGrupoSanguineo());
        paciente.setAltura(pacienteDto.getAltura());
        paciente.setPeso(pacienteDto.getPeso());
        paciente.setAlergias(pacienteDto.getAlergias());
        paciente.setEnfermedadesCronicas(pacienteDto.getEnfermedadesCronicas());
        paciente.setFechaRegistro(new Date());

        pacienteRepo.save(paciente);

        return "redirect:/panelpaciente/perfil";
    }
}

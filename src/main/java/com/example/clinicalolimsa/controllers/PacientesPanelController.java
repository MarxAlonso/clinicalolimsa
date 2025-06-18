package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.models.Citas;
import com.example.clinicalolimsa.models.Paciente;
import com.example.clinicalolimsa.repositories.CitasRepository;
import com.example.clinicalolimsa.repositories.PacienteRepository;
import com.example.clinicalolimsa.security.PacienteUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/pacientes")
public class PacientesPanelController {

    @Autowired
    private CitasRepository citasRepository;

    @GetMapping("/pacientepanel")
    public String mostrarVistaPaciente(Authentication authentication, Model model) {
        // Obtener el paciente autenticado
        PacienteUserDetails userDetails = (PacienteUserDetails) authentication.getPrincipal();
        Paciente paciente = userDetails.getPaciente();

        List<Citas> citas = citasRepository.findByPaciente(paciente);

        model.addAttribute("paciente", paciente);
        model.addAttribute("citas", citas);
        return "pacientes/pacientepanel";
    }
}

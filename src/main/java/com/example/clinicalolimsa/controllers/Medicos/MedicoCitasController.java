package com.example.clinicalolimsa.controllers.Medicos;

import com.example.clinicalolimsa.models.Citas;
import com.example.clinicalolimsa.models.Medicos;
import com.example.clinicalolimsa.models.Paciente;
import com.example.clinicalolimsa.repositories.CitasRepository;
import com.example.clinicalolimsa.repositories.PacienteRepository;
import com.example.clinicalolimsa.security.MedicoUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medicos/medicocitas")
public class MedicoCitasController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CitasRepository citasRepository;

    @GetMapping("/crear")
    public String mostrarFormularioCita(@RequestParam("pacienteId") Integer pacienteId,
                                        Authentication authentication, Model model) {
        MedicoUserDetails userDetails = (MedicoUserDetails) authentication.getPrincipal();
        Medicos medico = userDetails.getMedico();

        Paciente paciente = pacienteRepository.findById(pacienteId).orElseThrow();

        Citas nuevaCita = new Citas();
        nuevaCita.setPaciente(paciente);
        nuevaCita.setMedico(medico);
        nuevaCita.setEstado("Programada"); // Por defecto

        model.addAttribute("citas", nuevaCita);
        model.addAttribute("paciente", paciente);
        model.addAttribute("medico", medico);
        return "medicos/formulario_cita";
    }

    @PostMapping("/guardar")
    public String guardarCita(@ModelAttribute Citas cita) {
        citasRepository.save(cita);
        return "redirect:/medicos/medicospanel";
    }
}


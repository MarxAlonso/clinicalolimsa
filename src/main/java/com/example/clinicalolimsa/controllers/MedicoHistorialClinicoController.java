package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.models.Citas;
import com.example.clinicalolimsa.models.HistorialClinico;
import com.example.clinicalolimsa.models.Medicos;
import com.example.clinicalolimsa.models.Paciente;
import com.example.clinicalolimsa.repositories.CitasRepository;
import com.example.clinicalolimsa.repositories.HistorialClinicoRepository;
import com.example.clinicalolimsa.repositories.PacienteRepository;
import com.example.clinicalolimsa.security.MedicoUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/medicos/medicohistorial")
public class MedicoHistorialClinicoController {
    @Autowired
    private CitasRepository citasRepository;

    @Autowired
    private HistorialClinicoRepository historialClinicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;


    @PostMapping("/guardar")
    public String guardarHistorial(@ModelAttribute HistorialClinico historial,
                                   @RequestParam("citaId") Integer citaId,
                                   Authentication auth) {
        MedicoUserDetails userDetails = (MedicoUserDetails) auth.getPrincipal();
        Medicos medico = userDetails.getMedico();

        Citas cita = citasRepository.findById(citaId).orElseThrow();
        historial.setCita(cita);
        historial.setMedico(medico);
        historial.setPaciente(cita.getPaciente());
        historial.setFechaRegistro(LocalDateTime.now());

        historialClinicoRepository.save(historial);

        // Cambiar estado de la cita a "Completada"
        cita.setEstado("Completada");
        citasRepository.save(cita);

        return "redirect:/medicos/medicospanel";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(@RequestParam("citaId") Integer citaId, Model model) {
        Citas cita = citasRepository.findById(citaId).orElseThrow();

        model.addAttribute("cita", cita);
        model.addAttribute("paciente", cita.getPaciente());
        model.addAttribute("historial", new HistorialClinico());

        return "medicos/formulario_historial";
    }
    @GetMapping("/buscar")
    public String mostrarFormularioBusqueda() {
        return "medicos/buscar_paciente_historial";
    }

    @PostMapping("/buscar")
    public String buscarHistorialPorNombreODNI(@RequestParam("query") String query, Model model) {
        List<Paciente> pacientes = pacienteRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDocumentoIdentidad(query, query, query);

        model.addAttribute("pacientes", pacientes);
        return "medicos/resultados_busqueda_historial";
    }

    @GetMapping("/paciente/{id}")
    public String verHistorialPaciente(@PathVariable("id") Integer id, Model model) {
        List<HistorialClinico> historiales = historialClinicoRepository.findByPacienteId(id);

        model.addAttribute("historiales", historiales);
        return "medicos/historial_por_paciente";
    }
}

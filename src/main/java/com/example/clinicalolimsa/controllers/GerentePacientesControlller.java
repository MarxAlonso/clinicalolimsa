package com.example.clinicalolimsa.controllers;


import com.example.clinicalolimsa.models.Paciente;
import com.example.clinicalolimsa.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/gerente/gerentepaciente")
public class GerentePacientesControlller {
    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    public String listarPaciente(Model model) {
        model.addAttribute("paciente", pacienteRepository.findAll());
        return "gerente/gerentepaciente/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoPacienteForm(Model model) {
        model.addAttribute("paciente", new Paciente());
        return "gerente/gerentepaciente/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarPaciente(@PathVariable Integer id, Model model) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow();
        model.addAttribute("paciente", paciente);
        return "gerente/gerentepaciente/formulario";
    }

    @PostMapping("/guardar")
    public String guardarPaciente(@ModelAttribute Paciente paciente) {
        if (paciente.getId() == 0) {
            paciente.setRole("paciente");
            paciente.setCreatedAt(new Date());

            // Opcional: encriptar contraseña si deseas hacerlo
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            paciente.setPassword(passwordEncoder.encode(paciente.getPassword()));
        }

        pacienteRepository.save(paciente);
        return "redirect:/gerente/gerentepaciente";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPaciente(@PathVariable Integer id) {
        pacienteRepository.deleteById(id);
        return "redirect:/gerente/gerentepaciente";
    }
}

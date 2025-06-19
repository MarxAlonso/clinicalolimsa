package com.example.clinicalolimsa.controllers.Gerentes;


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
        model.addAttribute("pacientes", pacienteRepository.findAll());
        return "gerente/gerentepaciente/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoPacienteForm(Model model) {
        model.addAttribute("pacientes", new Paciente());
        return "gerente/gerentepaciente/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarPaciente(@PathVariable Integer id, Model model) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow();
        model.addAttribute("pacientes", paciente);
        return "gerente/gerentepaciente/formulario";
    }

    @PostMapping("/guardar")
    public String guardarPaciente(@ModelAttribute Paciente paciente) {
        if (paciente.getId() == null) {
            paciente.setRole("pacientes");
            paciente.setCreatedAt(new Date());

            // Encriptar la contraseña solo si es nuevo
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            paciente.setPassword(passwordEncoder.encode(paciente.getPassword()));
        } else {
            // Si estás editando, podrías decidir no sobreescribir la contraseña si está vacía
            if (paciente.getPassword() != null && !paciente.getPassword().isEmpty()) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                paciente.setPassword(passwordEncoder.encode(paciente.getPassword()));
            } else {
                // Opción: mantener la contraseña existente si no se envía una nueva
                Paciente existente = pacienteRepository.findById(paciente.getId()).orElseThrow();
                paciente.setPassword(existente.getPassword());
            }
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

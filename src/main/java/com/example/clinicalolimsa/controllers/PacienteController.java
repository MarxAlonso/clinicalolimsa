package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.models.AppUser;
import com.example.clinicalolimsa.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/gerente/pacientes")
public class PacienteController {

    @Autowired
    private AppUserRepository repo;

    @GetMapping
    public String listarPacientes(Model model) {
        List<AppUser> pacientes = repo.findAll()
                .stream()
                .filter(user -> "paciente".equals(user.getRole()))
                .collect(Collectors.toList());
        model.addAttribute("pacientes", pacientes);
        return "gestion_pacientes";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("paciente", new AppUser());
        return "paciente/form_paciente";
    }

    @PostMapping("/guardar")
    public String guardarPaciente(@ModelAttribute("paciente") AppUser paciente) {
        paciente.setRole("paciente");
        paciente.setCreatedAt(new Date());
        paciente.setPassword(new BCryptPasswordEncoder().encode(paciente.getPassword()));
        repo.save(paciente);
        return "redirect:/gerente/pacientes";
    }

    @GetMapping("/editar/{id}")
    public String editarPaciente(@PathVariable int id, Model model) {
        AppUser paciente = repo.findById(id).orElse(null);
        model.addAttribute("paciente", paciente);
        return "paciente/form_paciente";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPaciente(@PathVariable int id) {
        repo.deleteById(id);
        return "redirect:/gerente/pacientes";
    }
}

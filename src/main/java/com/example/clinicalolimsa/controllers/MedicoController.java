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

@Controller
@RequestMapping("/gerente/medicos")
public class MedicoController {

    @Autowired
    private AppUserRepository repo;

    @GetMapping
    public String listarMedicos(Model model) {
        List<AppUser> medicos = repo.findByRole("medico");
        model.addAttribute("medicos", medicos);
        return "gestion_medicos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("medico", new AppUser());
        return "medico/form_medico";
    }

    @PostMapping("/guardar")
    public String guardarMedico(@ModelAttribute("medico") AppUser medico) {
        medico.setRole("medico");
        medico.setCreatedAt(new Date());
        medico.setPassword(new BCryptPasswordEncoder().encode(medico.getPassword()));
        repo.save(medico);
        return "redirect:/gerente/medicos";
    }

    @GetMapping("/editar/{id}")
    public String editarMedico(@PathVariable int id, Model model) {
        AppUser medico = repo.findById(id).orElse(null);
        model.addAttribute("medico", medico);
        return "medico/form_medico";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMedico(@PathVariable int id) {
        repo.deleteById(id);
        return "redirect:/gerente/medicos";
    }
}


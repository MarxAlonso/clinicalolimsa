package com.example.clinicalolimsa.controllers.Gerentes;

import com.example.clinicalolimsa.models.Medicos;
import com.example.clinicalolimsa.repositories.MedicosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gerente/gerentemedicos")
public class GerenteMedicosController {
    @Autowired
    private MedicosRepository medicosRepository;
    @GetMapping
    public String listarMedicos(Model model) {
        model.addAttribute("medicos", medicosRepository.findAll());
        return "gerente/gerentemedicos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoMedicoForm(Model model) {
        model.addAttribute("medicos", new Medicos());
        return "gerente/gerentemedicos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarMedico(@PathVariable Integer id, Model model) {
        Medicos medico = medicosRepository.findById(id).orElseThrow();
        model.addAttribute("medicos", medico);
        return "gerente/gerentemedicos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarMedico(@ModelAttribute Medicos medico) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        medico.setContrasena(passwordEncoder.encode(medico.getContrasena()));
        medicosRepository.save(medico);
        return "redirect:/gerente/gerentemedicos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMedico(@PathVariable Integer id) {
        medicosRepository.deleteById(id);
        return "redirect:/gerente/gerentemedicos";
    }
}

package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.models.Medicamentos;
import com.example.clinicalolimsa.repositories.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/gerente/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @GetMapping
    public String listarMedicamentos(Model model) {
        model.addAttribute("medicamentos", medicamentoRepository.findAll());
        return "medicamentos/lista"; // vista HTML
    }

    @GetMapping("/nuevo")
    public String nuevoMedicamentoForm(Model model) {
        model.addAttribute("medicamento", new Medicamentos());
        return "medicamentos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarMedicamento(@ModelAttribute Medicamentos medicamento) {
        medicamento.setFechaRegistro(new Date());
        medicamentoRepository.save(medicamento);
        return "redirect:/gerente/medicamentos";
    }

    @GetMapping("/editar/{id}")
    public String editarMedicamento(@PathVariable int id, Model model) {
        Medicamentos medicamento = medicamentoRepository.findById(id).orElseThrow();
        model.addAttribute("medicamento", medicamento);
        return "medicamentos/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMedicamento(@PathVariable int id) {
        medicamentoRepository.deleteById(id);
        return "redirect:/gerente/medicamentos";
    }
}

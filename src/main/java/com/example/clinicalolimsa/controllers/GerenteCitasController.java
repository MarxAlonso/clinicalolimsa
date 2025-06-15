package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.models.Citas;
import com.example.clinicalolimsa.repositories.CitasRepository;
import com.example.clinicalolimsa.repositories.MedicosRepository;
import com.example.clinicalolimsa.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/gerente/gerentecitas")
public class GerenteCitasController {

    @Autowired
    private MedicosRepository medicosRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CitasRepository citasRepository;

    // Mostrar lista de citas
    @GetMapping
    public String listarCitas(Model model) {
        List<Citas> citas = citasRepository.findAll();
        model.addAttribute("citas", citas);
        return "gerente/gerentecitas/lista";
    }

    // Formulario para nueva cita
    @GetMapping("/nuevo")
    public String nuevaCitaForm(Model model) {
        model.addAttribute("citas", new Citas());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("medicos", medicosRepository.findAll());
        return "gerente/gerentecitas/formulario";
    }

    // Editar cita existente
    @GetMapping("/editar/{id}")
    public String editarCita(@PathVariable Integer id, Model model) {
        Citas cita = citasRepository.findById(id).orElseThrow();
        model.addAttribute("citas", cita);
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("medicos", medicosRepository.findAll());
        return "gerente/gerentecitas/formulario";
    }

    // Guardar cita (crear o actualizar)
    @PostMapping("/guardar")
    public String guardarCita(@ModelAttribute Citas citas) {
        citasRepository.save(citas);
        return "redirect:/gerente/gerentecitas";
    }

    // Eliminar cita
    @GetMapping("/eliminar/{id}")
    public String eliminarCita(@PathVariable Integer id) {
        citasRepository.deleteById(id);
        return "redirect:/gerente/gerentecitas";
    }
}
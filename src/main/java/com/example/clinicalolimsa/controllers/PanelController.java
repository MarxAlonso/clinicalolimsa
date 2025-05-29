package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.repositories.AppUserRepository;
import com.example.clinicalolimsa.repositories.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
public class PanelController {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @GetMapping("/panelgerente")
    public String panelGerente(Model model) {
        // Contar usuarios por rol
        long totalPacientes = userRepository.findByRole("paciente").size();
        long totalMedicos = userRepository.findByRole("medico").size();

        // Total medicamentos
        long totalMedicamentos = medicamentoRepository.count();

        // Pacientes por semana
        Map<String, Long> pacientesPorSemana = userRepository.findByRole("paciente").stream()
                .collect(Collectors.groupingBy(
                        u -> new SimpleDateFormat("YYYY-'S'ww").format(u.getCreatedAt()),
                        TreeMap::new,
                        Collectors.counting()
                ));

        // Medicamentos por semana
        Map<String, Long> medicamentosPorSemana = medicamentoRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        m -> new SimpleDateFormat("YYYY-'S'ww").format(m.getFechaRegistro()),
                        TreeMap::new,
                        Collectors.counting()
                ));

        // Enviamos al modelo
        model.addAttribute("totalPacientes", totalPacientes);
        model.addAttribute("totalMedicos", totalMedicos);
        model.addAttribute("totalMedicamentos", totalMedicamentos);
        model.addAttribute("pacientesPorSemana", pacientesPorSemana);
        model.addAttribute("medicamentosPorSemana", medicamentosPorSemana);

        return "panelgerente";
    }
    @GetMapping("/panelpaciente")
    public String panelPaciente() {
        return "panelpaciente"; // panelpaciente.html
    }

    @GetMapping("/panelmedico")
    public String panelMedico() {
        return "panelmedico"; // panelmedico.html
    }
}

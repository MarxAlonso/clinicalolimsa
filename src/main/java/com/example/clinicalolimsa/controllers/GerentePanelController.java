package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GerentePanelController {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private MedicosRepository medicoRepository;

    @Autowired
    private MedicamentosRepository medicamentosRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping("/gerente/gerentepanel")
    public String panelGerente(Model model) {
        model.addAttribute("totalMedicos", medicoRepository.count());
        model.addAttribute("totalMedicamentos", medicamentosRepository.count());
        model.addAttribute("totalPacientes", pacienteRepository.count());

        return "/gerente/gerentepanel";
    }

}

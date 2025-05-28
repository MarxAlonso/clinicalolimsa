package com.example.clinicalolimsa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PanelController {

    @GetMapping("/panelgerente")
    public String panelGerente() {
        return "panelgerente"; // panelgerente.html
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

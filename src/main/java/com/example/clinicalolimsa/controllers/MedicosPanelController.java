package com.example.clinicalolimsa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/medicos")
public class MedicosPanelController {
    @GetMapping("/medicospanel")
    public String mostrarVistaMedicos() {
        return "medicos/medicospanel";
    }
}

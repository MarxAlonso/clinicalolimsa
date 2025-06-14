package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class GerentePanelController {

    @Autowired
    private AppUserRepository userRepository;

    @GetMapping("/gerente/gerentepanel")
    public String panelGerente(Model model) {
        return "/gerente/gerentepanel";
    }

}
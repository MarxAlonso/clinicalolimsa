package com.example.clinicalolimsa.controllers.Medicos;

import com.example.clinicalolimsa.models.Citas;
import com.example.clinicalolimsa.models.Medicos;
import com.example.clinicalolimsa.repositories.CitasRepository;
import com.example.clinicalolimsa.security.MedicoUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/medicos")
public class MedicosPanelController {
    @Autowired
    private CitasRepository citasRepository;
    @GetMapping("/medicospanel")
    public String mostrarVistaMedicos(Authentication authentication, Model model) {
        // Obtener el médico desde la sesión de Spring Security
        MedicoUserDetails userDetails = (MedicoUserDetails) authentication.getPrincipal();
        Medicos medico = userDetails.getMedico();

        List<Citas> citasDelMedico = citasRepository.findByMedicoId(medico.getId());

        model.addAttribute("medico", medico);
        model.addAttribute("citas", citasDelMedico);

        return "medicos/medicospanel";
    }

}

package com.example.clinicalolimsa.controllers.Pacientes;


import com.example.clinicalolimsa.models.Paciente;
import com.example.clinicalolimsa.repositories.MedicamentosRepository;
import com.example.clinicalolimsa.repositories.TipoDeMedicamentoRepository;
import com.example.clinicalolimsa.security.PacienteUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pacientes/vermedicamentos")
public class PacienteMedicamentosController {

    @Autowired
    private MedicamentosRepository medicamentosRepository;

    @Autowired
    private TipoDeMedicamentoRepository tipoDeMedicamentoRepository;

    @GetMapping
    public String mostrarMedicamentos(Authentication authentication, Model model) {
        // Obtener el paciente autenticado
        PacienteUserDetails userDetails = (PacienteUserDetails) authentication.getPrincipal();
        Paciente paciente = userDetails.getPaciente();
        model.addAttribute("paciente", paciente);

        // Lista de medicamentos y tipos
        model.addAttribute("medicamentos", medicamentosRepository.findAll());
        model.addAttribute("tipos", tipoDeMedicamentoRepository.findAll());

        return "pacientes/vermedicamentos/vermedicamentos";
    }
}

package com.example.clinicalolimsa.controllers.Pacientes;

import com.example.clinicalolimsa.models.Compra;
import com.example.clinicalolimsa.models.Paciente;
import com.example.clinicalolimsa.repositories.CompraRepository;
import com.example.clinicalolimsa.security.PacienteUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/pacientes/vercompras")
public class PacienteComprasController {
    @Autowired
    private CompraRepository compraRepository;
    @GetMapping
    public String mostrarCompras(Authentication authentication, Model model, HttpServletRequest request) {
        PacienteUserDetails userDetails = (PacienteUserDetails) authentication.getPrincipal();
        Paciente paciente = userDetails.getPaciente();

        List<Compra> compras = compraRepository.findByPacienteOrderByFechaCompraDesc(paciente);

        model.addAttribute("compras", compras);
        model.addAttribute("paciente", paciente);

        // CSRF token
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", token);

        return "pacientes/vercompras/vercompras";
    }


}

package com.example.clinicalolimsa.controllers.Pacientes;


import com.example.clinicalolimsa.models.Compra;
import com.example.clinicalolimsa.models.Medicamentos;
import com.example.clinicalolimsa.models.Paciente;
import com.example.clinicalolimsa.repositories.CompraRepository;
import com.example.clinicalolimsa.repositories.MedicamentosRepository;
import com.example.clinicalolimsa.repositories.TipoDeMedicamentoRepository;
import com.example.clinicalolimsa.security.PacienteUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/pacientes/vermedicamentos")
public class PacienteMedicamentosController {

    @Autowired
    private MedicamentosRepository medicamentosRepository;

    @Autowired
    private TipoDeMedicamentoRepository tipoDeMedicamentoRepository;

    @GetMapping
    public String mostrarMedicamentos(Authentication authentication, Model model, HttpServletRequest request) {
        PacienteUserDetails userDetails = (PacienteUserDetails) authentication.getPrincipal();
        Paciente paciente = userDetails.getPaciente();
        model.addAttribute("paciente", paciente);
        model.addAttribute("medicamentos", medicamentosRepository.findAll());
        model.addAttribute("tipos", tipoDeMedicamentoRepository.findAll());

        // Agregar el token CSRF al modelo
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", token);

        return "pacientes/vermedicamentos/vermedicamentos";
    }

    @Autowired
    private CompraRepository compraRepository;

    @PostMapping("/comprar")
    public String procesarCompra(@RequestParam Integer medicamentoId,
                                 @RequestParam int cantidad,
                                 Authentication auth,
                                 RedirectAttributes redirectAttrs) {
        Medicamentos med = medicamentosRepository.findById(medicamentoId).orElseThrow();
        Paciente paciente = ((PacienteUserDetails) auth.getPrincipal()).getPaciente();

        if (med.getCantidad() < cantidad) {
            redirectAttrs.addFlashAttribute("error", "No hay suficiente stock disponible.");
            return "redirect:/pacientes/vermedicamentos";
        }

        double total = med.getPrecio() * cantidad;

        // Descontar stock
        med.setCantidad(med.getCantidad() - cantidad);
        medicamentosRepository.save(med);

        // Registrar compra
        Compra compra = new Compra();
        compra.setPaciente(paciente);
        compra.setMedicamento(med);
        compra.setCantidad(cantidad);
        compra.setTotal(total);
        compra.setFechaCompra(new Date());

        compraRepository.save(compra);

        redirectAttrs.addFlashAttribute("success", "Compra realizada correctamente.");
        return "redirect:/pacientes/vermedicamentos";
    }

}

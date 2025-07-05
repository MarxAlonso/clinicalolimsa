package com.example.clinicalolimsa.controllers.Gerentes;

import com.example.clinicalolimsa.models.Compra;
import com.example.clinicalolimsa.repositories.CompraRepository;
import com.example.clinicalolimsa.repositories.MedicamentosRepository;
import com.example.clinicalolimsa.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/gerente/gerentecompras")
public class GerenteComprasController {
    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private MedicamentosRepository medicamentosRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Mostrar todas las compras
    @GetMapping
    public String listarCompras(Model model) {
        List<Compra> compras = compraRepository.findAll();
        model.addAttribute("compras", compras);
        return "gerente/gerentecompras/lista"; // archivo HTML
    }
}

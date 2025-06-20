package com.example.clinicalolimsa.controllers.Gerentes;

import com.example.clinicalolimsa.models.Almacen;
import com.example.clinicalolimsa.repositories.AlmacenRepository;
import com.example.clinicalolimsa.repositories.MedicamentosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/gerente/gerentealmacen")
public class GerenteAlmacenController {

    @Autowired
    private AlmacenRepository almacenRepository;

    @Autowired
    private MedicamentosRepository medicamentosRepository;

    @GetMapping
    public String listarAlmacen(Model model) {
        model.addAttribute("almacenes", almacenRepository.findAll());
        return "gerente/gerentealmacen/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoAlmacenForm(Model model) {
        model.addAttribute("almacen", new Almacen());
        model.addAttribute("medicamentos", medicamentosRepository.findAll());
        return "gerente/gerentealmacen/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarAlmacen(@PathVariable Integer id, Model model) {
        Almacen almacen = almacenRepository.findById(id).orElseThrow();
        model.addAttribute("almacen", almacen);
        model.addAttribute("medicamentos", medicamentosRepository.findAll());
        return "gerente/gerentealmacen/formulario";
    }

    @PostMapping("/guardar")
    public String guardarAlmacen(@ModelAttribute Almacen almacen) {
        almacenRepository.save(almacen);
        return "redirect:/gerente/gerentealmacen";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAlmacen(@PathVariable Integer id) {
        almacenRepository.deleteById(id);
        return "redirect:/gerente/gerentealmacen";
    }
}

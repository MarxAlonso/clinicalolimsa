package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.models.Proveedor;
import com.example.clinicalolimsa.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gerente/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping
    public String listarProveedores(Model model) {
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "proveedores/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoProveedorForm(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "proveedores/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarProveedor(@PathVariable int id, Model model) {
        Proveedor proveedor = proveedorRepository.findById(id).orElseThrow();
        model.addAttribute("proveedor", proveedor);
        return "proveedores/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor) {
        proveedorRepository.save(proveedor);
        return "redirect:/gerente/proveedores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable int id) {
        proveedorRepository.deleteById(id);
        return "redirect:/gerente/proveedores";
    }
}

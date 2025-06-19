package com.example.clinicalolimsa.controllers.Gerentes;

import com.example.clinicalolimsa.models.Proveedor;
import com.example.clinicalolimsa.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/gerente/gerenteproveedores")
public class GerenteProveedorController {
    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping
    public String listarProveedores(Model model) {
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "gerente/gerenteproveedores/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoProveedorForm(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        return "gerente/gerenteproveedores/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarProveedor(@PathVariable Integer id, Model model) {
        Proveedor proveedor = proveedorRepository.findById(id).orElseThrow();
        model.addAttribute("proveedor", proveedor);
        return "gerente/gerenteproveedores/formulario";
    }

    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor) {
        if (proveedor.getId() == null) {
            proveedor.setFechaRegistro(new Date());
        }
        proveedorRepository.save(proveedor);
        return "redirect:/gerente/gerenteproveedores";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Integer id) {
        proveedorRepository.deleteById(id);
        return "redirect:/gerente/gerenteproveedores";
    }
}

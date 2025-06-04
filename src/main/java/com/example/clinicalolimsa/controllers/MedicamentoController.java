package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.models.Medicamentos;
import com.example.clinicalolimsa.repositories.MedicamentoRepository;
import com.example.clinicalolimsa.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/gerente/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @GetMapping
    public String listarMedicamentos(Model model) {
        model.addAttribute("medicamentos", medicamentoRepository.findAll());
        return "medicamentos/lista"; // vista HTML
    }

    @GetMapping("/nuevo")
    public String nuevoMedicamentoForm(Model model) {
        model.addAttribute("medicamento", new Medicamentos());
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "medicamentos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarMedicamento(@PathVariable int id, Model model) {
        Medicamentos medicamento = medicamentoRepository.findById(id).orElseThrow();
        model.addAttribute("medicamento", medicamento);
        model.addAttribute("proveedores", proveedorRepository.findAll());
        return "medicamentos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarMedicamento(@ModelAttribute Medicamentos medicamento,
                                     @RequestParam("imagenFile") MultipartFile imagenFile) {
        if (!imagenFile.isEmpty()) {
            try {
                String nombreArchivo = UUID.randomUUID().toString() + "_" + imagenFile.getOriginalFilename();
                Path ruta = Paths.get("uploads/" + nombreArchivo);
                Files.write(ruta, imagenFile.getBytes());
                medicamento.setImagen(nombreArchivo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        medicamento.setFechaRegistro(new Date());
        medicamentoRepository.save(medicamento);
        return "redirect:/gerente/medicamentos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMedicamento(@PathVariable int id) {
        medicamentoRepository.deleteById(id);
        return "redirect:/gerente/medicamentos";
    }
}

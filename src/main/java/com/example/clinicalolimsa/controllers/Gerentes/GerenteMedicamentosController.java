package com.example.clinicalolimsa.controllers.Gerentes;

import com.example.clinicalolimsa.models.Medicamentos;
import com.example.clinicalolimsa.repositories.MedicamentosRepository;
import com.example.clinicalolimsa.repositories.ProveedorRepository;
import com.example.clinicalolimsa.repositories.TipoDeMedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Controller
@RequestMapping("/gerente/gerentemedicamentos")
public class GerenteMedicamentosController {
    @Autowired
    private MedicamentosRepository medicamentosRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private TipoDeMedicamentoRepository tipoDeMedicamentoRepository;

    @GetMapping
    public String listarMedicamentos(Model model) {
        model.addAttribute("medicamentos", medicamentosRepository.findAll());

        return "gerente/gerentemedicamentos/lista"; // Vista HTML
    }

    @GetMapping("/nuevo")
    public String nuevoMedicamentoForm(Model model) {
        model.addAttribute("medicamentos", new Medicamentos());
        model.addAttribute("tipodemedicamentos", tipoDeMedicamentoRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());

        return "gerente/gerentemedicamentos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarMedicamentos(@PathVariable Integer id, Model model) {
        Medicamentos medicamentos = medicamentosRepository.findById(id).orElseThrow();

        model.addAttribute("medicamentos", medicamentos);

        model.addAttribute("tipodemedicamentos", tipoDeMedicamentoRepository.findAll());
        model.addAttribute("proveedores", proveedorRepository.findAll());

        return "gerente/gerentemedicamentos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarMedicamentos(@ModelAttribute Medicamentos medicamentos,
                                      @RequestParam("imagenFile") MultipartFile imagenFile) {
        if (!imagenFile.isEmpty()) {
            try {
                String originalFilename = imagenFile.getOriginalFilename();
                if (originalFilename != null &&
                        (originalFilename.endsWith(".jpg") ||
                                originalFilename.endsWith(".jpeg") ||
                                originalFilename.endsWith(".png") ||
                                originalFilename.endsWith(".webp"))) {

                    String nombreArchivo = UUID.randomUUID().toString() + "_" + originalFilename;
                    Path ruta = Paths.get("uploads/" + nombreArchivo);
                    Files.write(ruta, imagenFile.getBytes());

                    // Cambiar imagen solo si se sube una nueva
                    medicamentos.setImagen(nombreArchivo);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Si no se subió nueva imagen, conserva la existente
            if (medicamentos.getId() != null) {
                Medicamentos medicamentoExistente = medicamentosRepository.findById(medicamentos.getId()).orElse(null);
                if (medicamentoExistente != null) {
                    medicamentos.setImagen(medicamentoExistente.getImagen());
                }
            }
        }

        medicamentosRepository.save(medicamentos);
        return "redirect:/gerente/gerentemedicamentos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMedicamentos(@PathVariable Integer id) {
        medicamentosRepository.deleteById(id);

        return "redirect:/gerente/gerentemedicamentos";
    }
}
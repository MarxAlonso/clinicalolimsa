package com.example.clinicalolimsa.controllers.Gerentes;

import com.example.clinicalolimsa.models.Medicamentos;
import com.example.clinicalolimsa.repositories.MedicamentosRepository;
import com.example.clinicalolimsa.repositories.ProveedorRepository;
import com.example.clinicalolimsa.repositories.TipoDeMedicamentoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    @GetMapping("/exportar/excel")
    public void exportarMedicamentosExcel(HttpServletResponse response) throws IOException {
        List<Medicamentos> medicamentos = medicamentosRepository.findAll();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=medicamentos.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Medicamentos");

        int rowNum = 0;
        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue("Clínica Lolimsa - Reporte de Medicamentos");

        Row descripcion = sheet.createRow(rowNum++);
        descripcion.createCell(0).setCellValue("Este informe detalla los medicamentos registrados en la base de datos.");

        Row tableHeader = sheet.createRow(rowNum++);
        tableHeader.createCell(0).setCellValue("Nombre");
        tableHeader.createCell(1).setCellValue("Cantidad");
        tableHeader.createCell(2).setCellValue("Precio");
        tableHeader.createCell(3).setCellValue("Proveedor");
        tableHeader.createCell(4).setCellValue("Tipo");
        tableHeader.createCell(5).setCellValue("Fecha Vencimiento");

        for (Medicamentos med : medicamentos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(med.getNombre());
            row.createCell(1).setCellValue(med.getCantidad());
            row.createCell(2).setCellValue(med.getPrecio());
            row.createCell(3).setCellValue(med.getProveedor() != null ? med.getProveedor().getNombreEmpresa() : "");
            row.createCell(4).setCellValue(med.getTipo() != null ? med.getTipo().getNombre() : "");
            row.createCell(5).setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(med.getFechaVencimiento()));
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
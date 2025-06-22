package com.example.clinicalolimsa.controllers.Gerentes;

import com.example.clinicalolimsa.models.Medicos;
import com.example.clinicalolimsa.repositories.MedicosRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/gerente/gerentemedicos")
public class GerenteMedicosController {
    @Autowired
    private MedicosRepository medicosRepository;
    @GetMapping
    public String listarMedicos(Model model) {
        model.addAttribute("medicos", medicosRepository.findAll());
        return "gerente/gerentemedicos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoMedicoForm(Model model) {
        model.addAttribute("medicos", new Medicos());
        return "gerente/gerentemedicos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarMedico(@PathVariable Integer id, Model model) {
        Medicos medico = medicosRepository.findById(id).orElseThrow();
        model.addAttribute("medicos", medico);
        return "gerente/gerentemedicos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarMedico(@ModelAttribute Medicos medico) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        medico.setContrasena(passwordEncoder.encode(medico.getContrasena()));
        medicosRepository.save(medico);
        return "redirect:/gerente/gerentemedicos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMedico(@PathVariable Integer id) {
        medicosRepository.deleteById(id);
        return "redirect:/gerente/gerentemedicos";
    }
    @GetMapping("/exportar/excel")
    public void exportarMedicosExcel(HttpServletResponse response) throws IOException {
        List<Medicos> medicos = medicosRepository.findAll();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=medicos.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Médicos");

        // Estilo para título
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);

        // Estilo para encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Título
        Row tituloRow = sheet.createRow(0);
        Cell tituloCell = tituloRow.createCell(0);
        tituloCell.setCellValue("Clínica Lolimsa - Reporte de Médicos");
        tituloCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 9));

        // Fecha
        Row fechaRow = sheet.createRow(1);
        fechaRow.createCell(0).setCellValue("Fecha de reporte: " + LocalDate.now());

        // Encabezados
        String[] encabezados = {"ID", "Nombres", "Apellidos", "DNI", "Fecha Nac.", "Correo", "Teléfono", "Dirección", "Especialidad", "Colegiatura"};
        Row headerRow = sheet.createRow(3);
        for (int i = 0; i < encabezados.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(encabezados[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos
        int rowIdx = 4;
        for (Medicos medico : medicos) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(medico.getId());
            row.createCell(1).setCellValue(medico.getNombres());
            row.createCell(2).setCellValue(medico.getApellidos());
            row.createCell(3).setCellValue(medico.getDni());
            row.createCell(4).setCellValue(String.valueOf(medico.getFechaNacimiento()));
            row.createCell(5).setCellValue(medico.getCorreo());
            row.createCell(6).setCellValue(medico.getTelefono());
            row.createCell(7).setCellValue(medico.getDireccion());
            row.createCell(8).setCellValue(medico.getEspecialidad());
            row.createCell(9).setCellValue(medico.getNumeroColegiatura());
        }

        // Ajustar tamaño de columnas automáticamente
        for (int i = 0; i < encabezados.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

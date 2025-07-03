package com.example.clinicalolimsa.controllers.Gerentes;

import com.example.clinicalolimsa.models.Almacen;
import com.example.clinicalolimsa.models.Medicamentos;
import com.example.clinicalolimsa.repositories.AlmacenRepository;
import com.example.clinicalolimsa.repositories.MedicamentosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
    public String guardarAlmacen(@Valid @ModelAttribute("almacen") Almacen almacen,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("medicamentos", medicamentosRepository.findAll());
            return "gerente/gerentealmacen/formulario";
        }
        almacenRepository.save(almacen);
        return "redirect:/gerente/gerentealmacen";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAlmacen(@PathVariable Integer id) {
        almacenRepository.deleteById(id);
        return "redirect:/gerente/gerentealmacen";
    }
    @GetMapping("/exportar/excel")
    public void exportarAlmacenExcel(HttpServletResponse response) throws IOException {
        List<Almacen> almacenes = almacenRepository.findAll();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=almacen.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Almacén");

        // Título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Clínica Lolimsa – Reporte de Almacén");
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleStyle.setFont(titleFont);
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 3));

        // Fecha de generación
        Row dateRow = sheet.createRow(1);
        dateRow.createCell(0).setCellValue("Fecha de generación: " + java.time.LocalDate.now());

        // Encabezados
        String[] headers = { "ID", "Ubicación", "Fecha Ingreso", "Nombre Medicamento", "Descripción" };
        Row headerRow = sheet.createRow(3);
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos
        int rowNum = 4;
        for (Almacen almacen : almacenes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(almacen.getId());
            row.createCell(1).setCellValue(almacen.getUbicacion());
            row.createCell(2).setCellValue(
                    almacen.getFechaIngreso() != null
                            ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(almacen.getFechaIngreso())
                            : ""
            );
            Medicamentos med = almacen.getMedicamento();
            row.createCell(3).setCellValue(med != null ? med.getNombre() : "Sin medicamento");
            row.createCell(4).setCellValue(med != null ? med.getDescripcion() : "N/A");
        }

        // Autoajuste de columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

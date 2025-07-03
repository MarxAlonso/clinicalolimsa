package com.example.clinicalolimsa.controllers.Gerentes;

import com.example.clinicalolimsa.models.Proveedor;
import com.example.clinicalolimsa.repositories.ProveedorRepository;
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
    public String guardarProveedor(@Valid @ModelAttribute("proveedor") Proveedor proveedor,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "gerente/gerenteproveedores/formulario";
        }

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

    @GetMapping("/exportar/excel")
    public void exportarProveedoresExcel(HttpServletResponse response) throws IOException {
        List<Proveedor> proveedores = proveedorRepository.findAll();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=proveedores.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Proveedores");

        // Estilo título
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleStyle.setFont(titleFont);

        // Estilo encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Clínica Lolimsa – Reporte de Proveedores");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 7));

        // Fecha
        Row dateRow = sheet.createRow(1);
        dateRow.createCell(0).setCellValue("Fecha de generación: " + java.time.LocalDate.now());

        // Encabezados
        String[] headers = {
                "ID", "Nombre Empresa", "RUC", "Dirección", "Teléfono", "Gerente", "DNI Gerente",
                "Tipo Proveedor", "País Origen", "Fecha Registro"
        };

        Row headerRow = sheet.createRow(3);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos
        int rowNum = 4;
        for (Proveedor proveedor : proveedores) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(proveedor.getId());
            row.createCell(1).setCellValue(proveedor.getNombreEmpresa());
            row.createCell(2).setCellValue(proveedor.getRuc());
            row.createCell(3).setCellValue(proveedor.getDireccion());
            row.createCell(4).setCellValue(proveedor.getTelefono());
            row.createCell(5).setCellValue(proveedor.getNombreGerente());
            row.createCell(6).setCellValue(proveedor.getDniGerente());
            row.createCell(7).setCellValue(proveedor.getTipoProveedor());
            row.createCell(8).setCellValue(proveedor.getPaisOrigen());
            row.createCell(9).setCellValue(
                    proveedor.getFechaRegistro() != null ?
                            new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(proveedor.getFechaRegistro()) : ""
            );
        }

        // Ajuste columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

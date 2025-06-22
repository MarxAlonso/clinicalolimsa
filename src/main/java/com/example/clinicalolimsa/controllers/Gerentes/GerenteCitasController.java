package com.example.clinicalolimsa.controllers.Gerentes;

import com.example.clinicalolimsa.models.Citas;
import com.example.clinicalolimsa.repositories.CitasRepository;
import com.example.clinicalolimsa.repositories.MedicosRepository;
import com.example.clinicalolimsa.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/gerente/gerentecitas")
public class GerenteCitasController {

    @Autowired
    private MedicosRepository medicosRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CitasRepository citasRepository;

    // Mostrar lista de citas
    @GetMapping
    public String listarCitas(Model model) {
        List<Citas> citas = citasRepository.findAll();
        model.addAttribute("citas", citas);
        return "gerente/gerentecitas/lista";
    }

    // Formulario para nueva cita
    @GetMapping("/nuevo")
    public String nuevaCitaForm(Model model) {
        model.addAttribute("citas", new Citas());
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("medicos", medicosRepository.findAll());
        return "gerente/gerentecitas/formulario";
    }

    // Editar cita existente
    @GetMapping("/editar/{id}")
    public String editarCita(@PathVariable Integer id, Model model) {
        Citas cita = citasRepository.findById(id).orElseThrow();
        model.addAttribute("citas", cita);
        model.addAttribute("pacientes", pacienteRepository.findAll());
        model.addAttribute("medicos", medicosRepository.findAll());
        return "gerente/gerentecitas/formulario";
    }

    // Guardar cita (crear o actualizar)
    @PostMapping("/guardar")
    public String guardarCita(@ModelAttribute Citas citas) {
        citasRepository.save(citas);
        return "redirect:/gerente/gerentecitas";
    }

    // Eliminar cita
    @GetMapping("/eliminar/{id}")
    public String eliminarCita(@PathVariable Integer id) {
        citasRepository.deleteById(id);
        return "redirect:/gerente/gerentecitas";
    }


    @GetMapping("/exportar/excel")
    public void exportarCitasExcel(HttpServletResponse response) throws IOException {
        List<Citas> citas = citasRepository.findAll();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=citas.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Citas");

        // Estilo de título
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);
        titleStyle.setFont(titleFont);

        // Estilo de encabezado
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Clínica Lolimsa - Reporte de Citas");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 7));

        // Fecha del reporte
        Row dateRow = sheet.createRow(1);
        dateRow.createCell(0).setCellValue("Fecha de generación: " + LocalDate.now());

        // Encabezados
        String[] headers = {
                "ID", "Paciente", "Médico", "Fecha", "Hora", "Motivo", "Estado", "Observaciones"
        };
        Row headerRow = sheet.createRow(3);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos
        int rowNum = 4;
        for (Citas cita : citas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cita.getId());
            row.createCell(1).setCellValue(
                    cita.getPaciente().getFirstName() + " " + cita.getPaciente().getLastName()
            );
            row.createCell(2).setCellValue(
                    cita.getMedico().getNombres() + " " + cita.getMedico().getApellidos()
            );
            row.createCell(3).setCellValue(
                    cita.getFecha() != null ? cita.getFecha().toString() : ""
            );
            row.createCell(4).setCellValue(
                    cita.getHora() != null ? new java.text.SimpleDateFormat("HH:mm").format(cita.getHora()) : ""
            );
            row.createCell(5).setCellValue(cita.getMotivo());
            row.createCell(6).setCellValue(cita.getEstado());
            row.createCell(7).setCellValue(cita.getObservaciones());
        }

        // Ajuste de columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
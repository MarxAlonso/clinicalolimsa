package com.example.clinicalolimsa.controllers.Gerentes;


import com.example.clinicalolimsa.models.Paciente;
import com.example.clinicalolimsa.repositories.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/gerente/gerentepaciente")
public class GerentePacientesControlller {
    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    public String listarPaciente(Model model) {
        model.addAttribute("pacientes", pacienteRepository.findAll());
        return "gerente/gerentepaciente/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoPacienteForm(Model model) {
        model.addAttribute("pacientes", new Paciente());
        return "gerente/gerentepaciente/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarPaciente(@PathVariable Integer id, Model model) {
        Paciente paciente = pacienteRepository.findById(id).orElseThrow();
        model.addAttribute("pacientes", paciente);
        return "gerente/gerentepaciente/formulario";
    }

    @PostMapping("/guardar")
    public String guardarPaciente(@ModelAttribute Paciente paciente) {
        if (paciente.getId() == null) {
            paciente.setRole("pacientes");
            paciente.setCreatedAt(new Date());

            // Encriptar la contraseña solo si es nuevo
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            paciente.setPassword(passwordEncoder.encode(paciente.getPassword()));
        } else {
            // Si estás editando, podrías decidir no sobreescribir la contraseña si está vacía
            if (paciente.getPassword() != null && !paciente.getPassword().isEmpty()) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                paciente.setPassword(passwordEncoder.encode(paciente.getPassword()));
            } else {
                // Opción: mantener la contraseña existente si no se envía una nueva
                Paciente existente = pacienteRepository.findById(paciente.getId()).orElseThrow();
                paciente.setPassword(existente.getPassword());
            }
        }

        pacienteRepository.save(paciente);
        return "redirect:/gerente/gerentepaciente";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminarPaciente(@PathVariable Integer id) {
        pacienteRepository.deleteById(id);
        return "redirect:/gerente/gerentepaciente";
    }


    @GetMapping("/exportar/excel")
    public void exportarPacientesExcel(HttpServletResponse response) throws IOException {
        List<Paciente> pacientes = pacienteRepository.findAll();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=pacientes.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Pacientes");

        // Estilos
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Título
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Clínica Lolimsa - Reporte de Pacientes");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 6));

        // Fecha
        Row dateRow = sheet.createRow(1);
        dateRow.createCell(0).setCellValue("Fecha de reporte: " + LocalDate.now());

        // Encabezados
        String[] headers = {
                "ID", "Nombre", "Apellido", "Email", "Teléfono", "Dirección", "Sexo",
                "F. Nacimiento", "Tipo Sangre", "Doc. ID", "Nacionalidad", "Estado Civil",
                "Ocupación", "Contacto Emergencia", "Tel. Emergencia", "Relación",
                "Alergias", "Enfermedades", "Medicamentos"
        };

        Row headerRow = sheet.createRow(3);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Datos
        int rowIdx = 4;
        for (Paciente paciente : pacientes) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(paciente.getId());
            row.createCell(1).setCellValue(paciente.getFirstName());
            row.createCell(2).setCellValue(paciente.getLastName());
            row.createCell(3).setCellValue(paciente.getEmail());
            row.createCell(4).setCellValue(paciente.getPhone());
            row.createCell(5).setCellValue(paciente.getAddress());
            row.createCell(6).setCellValue(paciente.getSexo());
            row.createCell(7).setCellValue(paciente.getFechaNacimiento() != null ? paciente.getFechaNacimiento().toString() : "");
            row.createCell(8).setCellValue(paciente.getTipoSangre());
            row.createCell(9).setCellValue(paciente.getDocumentoIdentidad());
            row.createCell(10).setCellValue(paciente.getNacionalidad());
            row.createCell(11).setCellValue(paciente.getEstadoCivil());
            row.createCell(12).setCellValue(paciente.getOcupacion());
            row.createCell(13).setCellValue(paciente.getContactoEmergenciaNombre());
            row.createCell(14).setCellValue(paciente.getContactoEmergenciaTelefono());
            row.createCell(15).setCellValue(paciente.getContactoEmergenciaRelacion());
            row.createCell(16).setCellValue(paciente.getAlergias());
            row.createCell(17).setCellValue(paciente.getEnfermedadesPrevias());
            row.createCell(18).setCellValue(paciente.getMedicamentosActuales());
        }

        // Ajustar columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

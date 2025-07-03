package com.example.clinicalolimsa.controllers.Medicos;

import com.example.clinicalolimsa.models.Citas;
import com.example.clinicalolimsa.models.ExamenArterial;
import com.example.clinicalolimsa.models.Medicos;
import com.example.clinicalolimsa.models.Paciente;
import com.example.clinicalolimsa.repositories.CitasRepository;
import com.example.clinicalolimsa.repositories.ExamenArterialRepository;
import com.example.clinicalolimsa.security.MedicoUserDetails;
import java.io.IOException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.kernel.geom.PageSize;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/medicos/arterial")
public class MedicoExamenArterialController {

    @Autowired
    private CitasRepository citasRepository;

    @Autowired
    private ExamenArterialRepository examenArterialRepository;

    @GetMapping("/ver")
    public String verExamenes(Authentication authentication, Model model) {
        MedicoUserDetails userDetails = (MedicoUserDetails) authentication.getPrincipal();
        Medicos medico = userDetails.getMedico();

        List<ExamenArterial> examenes = examenArterialRepository.findByCita_Medico_Id(medico.getId());
        List<Citas> citas = citasRepository.findByMedicoId(medico.getId());

        // Filtrar las citas que aún no tienen examen
        Set<Integer> citasConExamen = examenes.stream()
                .map(e -> e.getCita().getId())
                .collect(Collectors.toSet());

        List<Citas> citasSinExamen = citas.stream()
                .filter(c -> !citasConExamen.contains(c.getId()))
                .toList();

        model.addAttribute("medico", medico);
        model.addAttribute("examenes", examenes);
        model.addAttribute("citasSinExamen", citasSinExamen);

        return "medicos/medicoexamenarterial/medpacexart";
    }


    @GetMapping("/nuevo/{citaId}")
    public String nuevoExamen(@PathVariable Integer citaId, Model model) {
        Citas cita = citasRepository.findById(citaId).orElseThrow();
        ExamenArterial examen = new ExamenArterial();
        examen.setCita(cita);
        model.addAttribute("examen", examen);
        return "medicos/medicoexamenarterial/formulario";
    }

    @PostMapping("/guardar")
    public String guardarExamen(@ModelAttribute ExamenArterial examen) {
        examenArterialRepository.save(examen);
        return "redirect:/medicos/arterial/ver";
    }

    @GetMapping("/resultado/{examenId}")
    public String verResultado(@PathVariable Integer examenId, Model model) {
        ExamenArterial examen = examenArterialRepository.findById(examenId).orElseThrow();
        model.addAttribute("examen", examen);

        // Valores saludables promedio
        model.addAttribute("sistolicaPromedio", 120);
        model.addAttribute("diastolicaPromedio", 80);

        return "medicos/medicoexamenarterial/resultado";
    }

    @GetMapping("/resultado/{examenId}/pdf")
    public void generarPdf(@PathVariable Integer examenId, HttpServletResponse response) throws IOException {
        ExamenArterial examen = examenArterialRepository.findById(examenId).orElseThrow();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=resultado_examen_" + examenId + ".pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(36, 36, 36, 36);

        // Título
        Paragraph titulo = new Paragraph("Resultado de Examen Arterial")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);
        document.add(titulo);

        // Información del paciente
        Citas cita = examen.getCita();
        Paciente paciente = cita.getPaciente();
        Medicos medico = cita.getMedico();

        Table infoTable = new Table(2).useAllAvailableWidth();
        infoTable.addCell(createCell("Información del Paciente", true));
        infoTable.addCell(createCell("Información del Médico", true));
        
        infoTable.addCell(createCell("Nombre: " + paciente.getFirstName() + " " + paciente.getLastName(), false));
        infoTable.addCell(createCell("Nombre: " + medico.getNombres() + " " + medico.getApellidos(), false));
        
        infoTable.addCell(createCell("DNI: " + paciente.getDocumentoIdentidad(), false));
        infoTable.addCell(createCell("N° Colegiatura: " + medico.getNumeroColegiatura(), false));
        
        document.add(infoTable.setMarginBottom(20));

        // Resultados del examen
        Table resultTable = new Table(2).useAllAvailableWidth();
        resultTable.addCell(createCell("Resultados del Examen", true, 2));
        
        resultTable.addCell(createCell("Fecha del examen:", false));
        resultTable.addCell(createCell(examen.getFechaRegistro().toString(), false));
        
        resultTable.addCell(createCell("Presión Sistólica:", false));
        resultTable.addCell(createCell(examen.getPresionSistolica() + " mmHg", false));
        
        resultTable.addCell(createCell("Presión Diastólica:", false));
        resultTable.addCell(createCell(examen.getPresionDiastolica() + " mmHg", false));
        
        document.add(resultTable.setMarginBottom(20));

        // Observaciones
        document.add(new Paragraph("Observaciones:")
                .setBold()
                .setMarginBottom(10));
        document.add(new Paragraph(examen.getObservaciones() != null ? examen.getObservaciones() : "Sin observaciones")
                .setMarginBottom(20));

        // Diagnóstico automático
        String diagnostico;
        if (examen.getPresionSistolica() >= 140 || examen.getPresionDiastolica() >= 90) {
            diagnostico = "Presión arterial alta (hipertensión)";
        } else if (examen.getPresionSistolica() < 90 || examen.getPresionDiastolica() < 60) {
            diagnostico = "Presión arterial baja (hipotensión)";
        } else {
            diagnostico = "Presión arterial normal";
        }

        Table diagTable = new Table(1).useAllAvailableWidth();
        diagTable.addCell(createCell("Diagnóstico", true));
        diagTable.addCell(createCell(diagnostico, false));
        document.add(diagTable);

        document.close();
    }

    private Cell createCell(String content, boolean isHeader) {
        Cell cell = new Cell().add(new Paragraph(content));
        if (isHeader) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);
        }
        cell.setBorder(new SolidBorder(ColorConstants.BLACK, 1));
        return cell;
    }

    private Cell createCell(String content, boolean isHeader, int colspan) {
        Cell cell = createCell(content, isHeader);
        cell.setWidth(colspan);
        return cell;
    }


}


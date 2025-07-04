package com.example.clinicalolimsa.controllers.Medicos;

import com.example.clinicalolimsa.models.Citas;
import com.example.clinicalolimsa.models.ExamenArterial;
import com.example.clinicalolimsa.models.Medicos;
import com.example.clinicalolimsa.repositories.CitasRepository;
import com.example.clinicalolimsa.repositories.ExamenArterialRepository;
import com.example.clinicalolimsa.security.MedicoUserDetails;
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

}


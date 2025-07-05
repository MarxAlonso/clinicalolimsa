package com.example.clinicalolimsa.controllers.Medicos;

import com.example.clinicalolimsa.models.Citas;
import com.example.clinicalolimsa.models.ExamenArterial;
import com.example.clinicalolimsa.models.ExamenSangre;
import com.example.clinicalolimsa.models.Medicos;
import com.example.clinicalolimsa.repositories.CitasRepository;
import com.example.clinicalolimsa.repositories.ExamenSangreRepository;
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
@RequestMapping("/medicos/sangre")
public class MedicoExamenSangreController {

    @Autowired
    private ExamenSangreRepository examenSangreRepository;

    @Autowired
    private CitasRepository citasRepository;

    @GetMapping("/ver")
    public String verExamenes(Authentication authentication, Model model) {
        MedicoUserDetails userDetails = (MedicoUserDetails) authentication.getPrincipal();
        Medicos medico = userDetails.getMedico();

        List<ExamenSangre> examenes = examenSangreRepository.findByCita_Medico_Id(medico.getId());
        List<Citas> citas = citasRepository.findByMedicoId(medico.getId());

        // Filtrar las citas sin examen de sangre
        Set<Integer> citasConExamen = examenes.stream()
                .map(e -> e.getCita().getId())
                .collect(Collectors.toSet());

        List<Citas> citasSinExamen = citas.stream()
                .filter(c -> !citasConExamen.contains(c.getId()))
                .toList();

        model.addAttribute("medico", medico);
        model.addAttribute("examenes", examenes);
        model.addAttribute("citasSinExamen", citasSinExamen);

        return "medicos/medicoexamensangre/medpacexsangre";
    }

    @GetMapping("/nuevo/{citaId}")
    public String nuevoExamen(@PathVariable Integer citaId, Model model) {
        Citas cita = citasRepository.findById(citaId).orElseThrow();
        ExamenSangre examen = new ExamenSangre();
        examen.setCita(cita);
        model.addAttribute("examen", examen);
        return "medicos/medicoexamensangre/formulario";
    }

    @PostMapping("/guardar")
    public String guardarExamen(@ModelAttribute ExamenSangre examen) {
        examenSangreRepository.save(examen);
        return "redirect:/medicos/sangre/ver";
    }

    @GetMapping("/resultado/{id}")
    public String verResultado(@PathVariable Integer id, Model model) {
        ExamenSangre examen = examenSangreRepository.findById(id).orElseThrow();
        model.addAttribute("examen", examen);

        // Promedios de referencia (puedes ajustar según necesidad médica)
        model.addAttribute("promedioHemoglobina", 14.0);
        model.addAttribute("promedioGlucosa", 90.0);
        model.addAttribute("promedioColesterol", 180.0);

        return "medicos/medicoexamensangre/resultado";
    }
}


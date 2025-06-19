package com.example.clinicalolimsa.controllers.Gerentes;

import com.example.clinicalolimsa.models.TipoDeMedicamento;
import com.example.clinicalolimsa.repositories.TipoDeMedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/gerente/gerentetipodemedicamentos")
public class GerenteTipoMedicamentoController {
    @Autowired
    private TipoDeMedicamentoRepository tipoDeMedicamentoRepository;

    @GetMapping
    public String listarTipoMedicamentos(Model model) {
        model.addAttribute("tipodemedicamentos", tipoDeMedicamentoRepository.findAll());

        return "gerente/gerentetipodemedicamentos/lista"; // Vista HTML
    }

    @GetMapping("/nuevo")
    public String nuevoTipoMedicamentoForm(Model model) {
        model.addAttribute("tipodemedicamentos", new TipoDeMedicamento());

        return "gerente/gerentetipodemedicamentos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarTipoMedicamentos(@PathVariable Integer id, Model model) {
        TipoDeMedicamento tipoDeMedicamento = tipoDeMedicamentoRepository.findById(id).orElseThrow();
        model.addAttribute("tipodemedicamentos", tipoDeMedicamento);
        return "gerente/gerentetipodemedicamentos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarTipoMedicamentos(@ModelAttribute TipoDeMedicamento tipoDeMedicamento) {
        tipoDeMedicamentoRepository.save(tipoDeMedicamento);
        return "redirect:/gerente/gerentetipodemedicamentos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTipoMedicamentos(@PathVariable Integer id) {
        tipoDeMedicamentoRepository.deleteById(id);
        return "redirect:/gerente/gerentemtipodemedicamentos";
    }

}

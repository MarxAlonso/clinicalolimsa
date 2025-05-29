package com.example.clinicalolimsa.services;

import com.example.clinicalolimsa.models.AppUser;
import com.example.clinicalolimsa.models.EstadisticasDTO;
import com.example.clinicalolimsa.models.Medicamentos;
import com.example.clinicalolimsa.repositories.AppUserRepository;
import com.example.clinicalolimsa.repositories.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstadisticasService {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public EstadisticasDTO obtenerEstadisticas() {
        EstadisticasDTO dto = new EstadisticasDTO();

        dto.setTotalPacientes(userRepository.findByRole("paciente").size());
        dto.setTotalMedicos(userRepository.findByRole("medico").size());
        dto.setTotalMedicamentos(medicamentoRepository.count());

        dto.setPacientesPorSemana(contarPorSemana(userRepository.findByRole("paciente")));
        dto.setMedicamentosPorSemana(contarPorSemana(medicamentoRepository.findAll()));

        return dto;
    }

    private List<Long> contarPorSemana(List<?> lista) {
        List<Long> resultado = new ArrayList<>();
        LocalDate hoy = LocalDate.now();

        for (int i = 5; i >= 0; i--) {
            LocalDate inicio = hoy.minusWeeks(i).with(DayOfWeek.MONDAY);
            LocalDate fin = inicio.plusDays(6);

            long count = lista.stream()
                    .filter(obj -> {
                        Date fecha = null;
                        if (obj instanceof AppUser) fecha = ((AppUser) obj).getCreatedAt();
                        else if (obj instanceof Medicamentos) fecha = ((Medicamentos) obj).getFechaRegistro();
                        if (fecha == null) return false;

                        LocalDate fechaLocal = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return !fechaLocal.isBefore(inicio) && !fechaLocal.isAfter(fin);
                    })
                    .count();

            resultado.add(count);
        }

        return resultado;
    }
    public Map<String, Long> getPacientesPorSemana() {
        return userRepository.findByRole("paciente").stream()
                .collect(Collectors.groupingBy(
                        u -> new SimpleDateFormat("YYYY-'S'ww").format(u.getCreatedAt()),
                        TreeMap::new,
                        Collectors.counting()
                ));
    }

    public Map<String, Long> getMedicamentosPorSemana() {
        return medicamentoRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        m -> new SimpleDateFormat("YYYY-'S'ww").format(m.getFechaRegistro()),
                        TreeMap::new,
                        Collectors.counting()
                ));
    }

}

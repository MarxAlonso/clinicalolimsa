package com.example.clinicalolimsa.services;

import com.example.clinicalolimsa.models.Medicos;
import com.example.clinicalolimsa.repositories.MedicosRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mockito Tests - Medicos Repository")
class MedicosMockitoTest {

    @Mock
    private MedicosRepository medicosRepository;

    @Test
    @DisplayName("Should save medico successfully")
    void testSaveMedico() {

        // Arrange
        Medicos medico =
                createMedico(
                        null,
                        "Carlos",
                        "Perez",
                        "12345678"
                );

        Medicos savedMedico =
                createMedico(
                        1,
                        "Carlos",
                        "Perez",
                        "12345678"
                );

        when(medicosRepository.save(any(Medicos.class)))
                .thenReturn(savedMedico);

        // Act
        Medicos result =
                medicosRepository.save(medico);

        // Assert
        assertNotNull(result.getId());

        assertEquals("Carlos",
                result.getNombres());

        assertEquals("Perez",
                result.getApellidos());

        // Verify
        verify(medicosRepository)
                .save(any(Medicos.class));
    }

    @Test
    @DisplayName("Should find medico by ID")
    void testFindById() {

        // Arrange
        Integer medicoId = 1;

        Medicos medico =
                createMedico(
                        medicoId,
                        "Ana",
                        "Gomez",
                        "87654321"
                );

        when(medicosRepository.findById(medicoId))
                .thenReturn(Optional.of(medico));

        // Act
        Optional<Medicos> result =
                medicosRepository.findById(medicoId);

        // Assert
        assertTrue(result.isPresent());

        assertEquals("Ana",
                result.get().getNombres());

        // Verify
        verify(medicosRepository)
                .findById(medicoId);
    }

    @Test
    @DisplayName("Should return all medicos")
    void testFindAllMedicos() {

        // Arrange
        Medicos m1 =
                createMedico(
                        1,
                        "Luis",
                        "Torres",
                        "11111111"
                );

        Medicos m2 =
                createMedico(
                        2,
                        "Maria",
                        "Lopez",
                        "22222222"
                );

        List<Medicos> medicos =
                Arrays.asList(m1, m2);

        when(medicosRepository.findAll())
                .thenReturn(medicos);

        // Act
        List<Medicos> result =
                medicosRepository.findAll();

        // Assert
        assertEquals(2, result.size());

        // Verify
        verify(medicosRepository)
                .findAll();
    }

    @Test
    @DisplayName("Should delete medico successfully")
    void testDeleteMedico() {

        // Arrange
        Integer medicoId = 1;

        doNothing().when(medicosRepository)
                .deleteById(medicoId);

        // Act
        medicosRepository.deleteById(medicoId);

        // Assert
        verify(medicosRepository)
                .deleteById(medicoId);

        verify(medicosRepository, never())
                .save(any());
    }

    @Test
    @DisplayName("Should verify medico arguments using captor")
    void testSaveMedico_WithCaptor() {

        // Arrange
        ArgumentCaptor<Medicos> captor =
                ArgumentCaptor.forClass(Medicos.class);

        Medicos medico =
                createMedico(
                        null,
                        "Pedro",
                        "Ramirez",
                        "99999999"
                );

        when(medicosRepository.save(any(Medicos.class)))
                .thenReturn(medico);

        // Act
        medicosRepository.save(medico);

        // Assert
        verify(medicosRepository)
                .save(captor.capture());

        Medicos captured =
                captor.getValue();

        assertEquals("Pedro",
                captured.getNombres());

        assertEquals("Ramirez",
                captured.getApellidos());

        assertEquals("99999999",
                captured.getDni());
    }

    @Test
    @DisplayName("Should handle multiple count calls")
    void testCountMedicos() {

        // Arrange
        when(medicosRepository.count())
                .thenReturn(5L)
                .thenReturn(6L);

        // Act
        long first =
                medicosRepository.count();

        long second =
                medicosRepository.count();

        // Assert
        assertEquals(5L, first);
        assertEquals(6L, second);
    }

    @Test
    @DisplayName("Should verify method never called")
    void testMethodNeverCalled() {

        // Arrange
        Medicos medico =
                createMedico(
                        1,
                        "Jose",
                        "Mendoza",
                        "44444444"
                );

        when(medicosRepository.findById(1))
                .thenReturn(Optional.of(medico));

        // Act
        Optional<Medicos> result =
                medicosRepository.findById(1);

        // Assert
        assertTrue(result.isPresent());

        verify(medicosRepository, never())
                .deleteById(any());
    }

    // ======================================================
    // HELPER METHOD
    // ======================================================

    private Medicos createMedico(
            Integer id,
            String nombres,
            String apellidos,
            String dni
    ) {

        Medicos medico = new Medicos();

        medico.setId(id);
        medico.setNombres(nombres);
        medico.setApellidos(apellidos);
        medico.setDni(dni);

        medico.setCorreo("medico@gmail.com");
        medico.setContrasena("123456");
        medico.setTelefono("999999999");
        medico.setDireccion("Lima");

        medico.setSueldo(5000.0);

        medico.setFechaNacimiento(
                LocalDate.of(1985, 5, 10)
        );

        medico.setEspecialidad("Cardiología");

        medico.setNumeroColegiatura("CMP12345");

        medico.setUniversidad("UNMSM");

        medico.setFechaGraduacion(
                LocalDate.of(2010, 12, 15)
        );

        medico.setTiempoExperiencia(10);

        medico.setTurno("Mañana");

        medico.setGenero("Masculino");

        medico.setFechaContratacion(
                LocalDate.of(2020, 1, 1)
        );

        return medico;
    }
}
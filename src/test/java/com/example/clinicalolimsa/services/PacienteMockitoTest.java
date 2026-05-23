package com.example.clinicalolimsa.services;


import com.example.clinicalolimsa.models.Paciente;
import com.example.clinicalolimsa.repositories.PacienteRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mockito Tests - Paciente Repository")
class PacienteMockitoTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Test
    @DisplayName("Should find patient by email")
    void testFindByEmail() {

        // Arrange
        Paciente paciente = createPaciente(
                1,
                "Juan",
                "Perez",
                "juan@gmail.com"
        );

        when(pacienteRepository.findByEmail("juan@gmail.com"))
                .thenReturn(paciente);

        // Act
        Paciente result =
                pacienteRepository.findByEmail("juan@gmail.com");

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getFirstName());
        assertEquals("Perez", result.getLastName());

        // Verify
        verify(pacienteRepository, times(1))
                .findByEmail("juan@gmail.com");
    }

    @Test
    @DisplayName("Should verify email exists")
    void testExistsByEmail() {

        // Arrange
        when(pacienteRepository.existsByEmail("test@gmail.com"))
                .thenReturn(true);

        // Act
        boolean exists =
                pacienteRepository.existsByEmail("test@gmail.com");

        // Assert
        assertTrue(exists);

        // Verify
        verify(pacienteRepository)
                .existsByEmail("test@gmail.com");
    }

    @Test
    @DisplayName("Should find patients by role")
    void testFindByRole() {

        // Arrange
        Paciente p1 = createPaciente(
                1,
                "Carlos",
                "Lopez",
                "carlos@gmail.com"
        );

        p1.setRole("PATIENT");

        Paciente p2 = createPaciente(
                2,
                "Maria",
                "Gomez",
                "maria@gmail.com"
        );

        p2.setRole("PATIENT");

        List<Paciente> pacientes =
                Arrays.asList(p1, p2);

        when(pacienteRepository.findByRole("PATIENT"))
                .thenReturn(pacientes);

        // Act
        List<Paciente> result =
                pacienteRepository.findByRole("PATIENT");

        // Assert
        assertEquals(2, result.size());

        // Verify
        verify(pacienteRepository)
                .findByRole("PATIENT");
    }

    @Test
    @DisplayName("Should save patient successfully")
    void testSavePaciente() {

        // Arrange
        Paciente paciente = createPaciente(
                null,
                "Luis",
                "Torres",
                "luis@gmail.com"
        );

        Paciente savedPaciente = createPaciente(
                1,
                "Luis",
                "Torres",
                "luis@gmail.com"
        );

        when(pacienteRepository.save(any(Paciente.class)))
                .thenReturn(savedPaciente);

        // Act
        Paciente result =
                pacienteRepository.save(paciente);

        // Assert
        assertNotNull(result.getId());
        assertEquals("Luis", result.getFirstName());

        // Verify
        verify(pacienteRepository)
                .save(any(Paciente.class));
    }

    @Test
    @DisplayName("Should capture saved patient arguments")
    void testSavePaciente_WithCaptor() {

        // Arrange
        ArgumentCaptor<Paciente> captor =
                ArgumentCaptor.forClass(Paciente.class);

        Paciente paciente = createPaciente(
                null,
                "Ana",
                "Ramirez",
                "ana@gmail.com"
        );

        when(pacienteRepository.save(any(Paciente.class)))
                .thenReturn(paciente);

        // Act
        pacienteRepository.save(paciente);

        // Assert
        verify(pacienteRepository)
                .save(captor.capture());

        Paciente captured =
                captor.getValue();

        assertEquals("Ana",
                captured.getFirstName());

        assertEquals("Ramirez",
                captured.getLastName());

        assertEquals("ana@gmail.com",
                captured.getEmail());
    }

    @Test
    @DisplayName("Should find patient by search filters")
    void testSearchPaciente() {

        // Arrange
        Paciente paciente = createPaciente(
                1,
                "Pedro",
                "Martinez",
                "pedro@gmail.com"
        );

        paciente.setDocumentoIdentidad("12345678");

        List<Paciente> pacientes =
                Arrays.asList(paciente);

        when(pacienteRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDocumentoIdentidadContaining(
                        "Pedro",
                        "Pedro",
                        "Pedro"
                ))
                .thenReturn(pacientes);

        // Act
        List<Paciente> result =
                pacienteRepository
                        .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDocumentoIdentidadContaining(
                                "Pedro",
                                "Pedro",
                                "Pedro"
                        );

        // Assert
        assertEquals(1, result.size());

        assertEquals("Pedro",
                result.get(0).getFirstName());

        // Verify
        verify(pacienteRepository)
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrDocumentoIdentidadContaining(
                        "Pedro",
                        "Pedro",
                        "Pedro"
                );
    }

    @Test
    @DisplayName("Should delete patient successfully")
    void testDeletePaciente() {

        // Arrange
        Integer pacienteId = 1;

        doNothing().when(pacienteRepository)
                .deleteById(pacienteId);

        // Act
        pacienteRepository.deleteById(pacienteId);

        // Assert
        verify(pacienteRepository)
                .deleteById(pacienteId);

        verify(pacienteRepository, never())
                .save(any());
    }

    @Test
    @DisplayName("Should count repository calls")
    void testCountPatients() {

        // Arrange
        when(pacienteRepository.count())
                .thenReturn(10L);

        // Act
        long total =
                pacienteRepository.count();

        // Assert
        assertEquals(10L, total);

        // Verify
        verify(pacienteRepository)
                .count();
    }

    // =====================================================
    // HELPER METHOD
    // =====================================================

    private Paciente createPaciente(
            Integer id,
            String firstName,
            String lastName,
            String email
    ) {

        Paciente paciente = new Paciente();

        paciente.setId(id);
        paciente.setFirstName(firstName);
        paciente.setLastName(lastName);
        paciente.setEmail(email);

        paciente.setPhone("999999999");
        paciente.setAddress("Lima");
        paciente.setPassword("123456");
        paciente.setRole("PATIENT");

        paciente.setCreatedAt(new Date());

        paciente.setSexo("Masculino");
        paciente.setFechaNacimiento(
                LocalDate.of(2000, 1, 1)
        );

        paciente.setTipoSangre("O+");
        paciente.setDocumentoIdentidad("12345678");
        paciente.setNacionalidad("Peruana");

        paciente.setEstadoCivil("Soltero");
        paciente.setOcupacion("Estudiante");

        paciente.setContactoEmergenciaNombre("Maria");
        paciente.setContactoEmergenciaTelefono("987654321");
        paciente.setContactoEmergenciaRelacion("Madre");

        paciente.setAlergias("Penicilina");
        paciente.setEnfermedadesPrevias("Ninguna");
        paciente.setMedicamentosActuales("Paracetamol");

        return paciente;
    }
}
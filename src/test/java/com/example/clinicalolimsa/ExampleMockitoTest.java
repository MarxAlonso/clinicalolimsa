package com.example.clinicalolimsa;
import com.example.clinicalolimsa.models.Medicamentos;
import com.example.clinicalolimsa.repositories.MedicamentosRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Mockito Tests - Medicamentos Repository")
class ExampleMockitoTest {

    @Mock
    private MedicamentosRepository medicinesRepository;

    @Test
    @DisplayName("Should return medicine when ID exists")
    void testFindMedicineById_Success() {

        // Arrange
        Integer medicineId = 1;

        Medicamentos medicine = new Medicamentos();
        medicine.setId(medicineId);
        medicine.setNombre("Aspirina");
        medicine.setPrecio(10.50);
        medicine.setFechaRegistro(new Date());

        when(medicinesRepository.findById(medicineId))
                .thenReturn(Optional.of(medicine));

        // Act
        Optional<Medicamentos> result =
                medicinesRepository.findById(medicineId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Aspirina", result.get().getNombre());
        assertEquals(10.50, result.get().getPrecio());

        // Verify
        verify(medicinesRepository, times(1))
                .findById(medicineId);
    }

    @Test
    @DisplayName("Should return empty when medicine not found")
    void testFindMedicineById_NotFound() {

        // Arrange
        Integer nonExistentId = 999;

        when(medicinesRepository.findById(nonExistentId))
                .thenReturn(Optional.empty());

        // Act
        Optional<Medicamentos> result =
                medicinesRepository.findById(nonExistentId);

        // Assert
        assertFalse(result.isPresent());

        // Verify
        verify(medicinesRepository)
                .findById(nonExistentId);
    }

    @Test
    @DisplayName("Should find all medicines")
    void testFindAllMedicines() {

        // Arrange
        Medicamentos med1 =
                createMedicine(1, "Aspirina", 10.0);

        Medicamentos med2 =
                createMedicine(2, "Ibuprofeno", 15.0);

        Medicamentos med3 =
                createMedicine(3, "Paracetamol", 8.0);

        List<Medicamentos> medicines =
                Arrays.asList(med1, med2, med3);

        when(medicinesRepository.findAll())
                .thenReturn(medicines);

        // Act
        List<Medicamentos> result =
                medicinesRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Aspirina",
                result.get(0).getNombre());

        // Verify
        verify(medicinesRepository, atLeastOnce())
                .findAll();
    }

    @Test
    @DisplayName("Should save medicine successfully")
    void testSaveMedicine() {

        // Arrange
        Medicamentos medicine = new Medicamentos();
        medicine.setNombre("NuevoMedicamento");
        medicine.setPrecio(25.0);
        medicine.setFechaRegistro(new Date());

        Medicamentos savedMedicine = new Medicamentos();
        savedMedicine.setId(1);
        savedMedicine.setNombre(medicine.getNombre());
        savedMedicine.setPrecio(medicine.getPrecio());
        savedMedicine.setFechaRegistro(new Date());

        when(medicinesRepository.save(any(Medicamentos.class)))
                .thenReturn(savedMedicine);

        // Act
        Medicamentos result =
                medicinesRepository.save(medicine);

        // Assert
        assertNotNull(result.getId());
        assertEquals("NuevoMedicamento",
                result.getNombre());

        // Verify
        verify(medicinesRepository)
                .save(any(Medicamentos.class));
    }

    @Test
    @DisplayName("Should verify exact arguments using ArgumentCaptor")
    void testSaveMedicine_WithArgumentCaptor() {

        // Arrange
        ArgumentCaptor<Medicamentos> captor =
                ArgumentCaptor.forClass(Medicamentos.class);

        Medicamentos medicine =
                createMedicine(null, "TestMed", 50.0);

        Medicamentos savedMedicine =
                createMedicine(1, "TestMed", 50.0);

        when(medicinesRepository.save(any(Medicamentos.class)))
                .thenReturn(savedMedicine);

        // Act
        medicinesRepository.save(medicine);

        // Assert
        verify(medicinesRepository)
                .save(captor.capture());

        Medicamentos capturedMedicine =
                captor.getValue();

        assertEquals("TestMed",
                capturedMedicine.getNombre());

        assertEquals(50.0,
                capturedMedicine.getPrecio());
    }

    @Test
    @DisplayName("Should delete medicine successfully")
    void testDeleteMedicine() {

        // Arrange
        Integer medicineId = 1;

        doNothing().when(medicinesRepository)
                .deleteById(medicineId);

        // Act
        medicinesRepository.deleteById(medicineId);

        // Assert
        verify(medicinesRepository)
                .deleteById(medicineId);

        verify(medicinesRepository, never())
                .save(any());
    }

    @Test
    @DisplayName("Should handle multiple consecutive calls")
    void testMultipleCalls() {

        // Arrange
        when(medicinesRepository.count())
                .thenReturn(5L)
                .thenReturn(6L)
                .thenReturn(7L);

        // Act
        long count1 = medicinesRepository.count();
        long count2 = medicinesRepository.count();
        long count3 = medicinesRepository.count();

        // Assert
        assertEquals(5L, count1);
        assertEquals(6L, count2);
        assertEquals(7L, count3);
    }

    @Test
    @DisplayName("Should search medicines by name")
    void testSearchByName() {

        // Arrange
        List<Medicamentos> results = Arrays.asList(
                createMedicine(1, "Aspirina", 10.0)
        );

        when(medicinesRepository.findAll())
                .thenReturn(results);

        // Act
        List<Medicamentos> foundMedicines =
                medicinesRepository.findAll();

        // Assert
        assertEquals(1, foundMedicines.size());

        assertTrue(foundMedicines.get(0)
                .getNombre()
                .contains("Aspirina"));
    }

    @Test
    @DisplayName("Should verify method never called")
    void testMethodNeverCalled() {

        // Arrange
        Medicamentos medicine =
                createMedicine(1, "Medicine", 10.0);

        when(medicinesRepository.findById(1))
                .thenReturn(Optional.of(medicine));

        // Act
        Optional<Medicamentos> result =
                medicinesRepository.findById(1);

        // Assert
        assertTrue(result.isPresent());

        verify(medicinesRepository, never())
                .deleteById(any());
    }

    @Test
    @DisplayName("Should reset mock between tests")
    void testMockReset() {

        // Arrange
        when(medicinesRepository.count())
                .thenReturn(5L);

        assertEquals(5L,
                medicinesRepository.count());

        // Reset
        reset(medicinesRepository);

        when(medicinesRepository.count())
                .thenReturn(0L);

        // Assert
        assertEquals(0L,
                medicinesRepository.count());
    }

    // ======================================================
    // HELPER METHOD
    // ======================================================

    private Medicamentos createMedicine(
            Integer id,
            String nombre,
            Double precio
    ) {

        Medicamentos medicine = new Medicamentos();

        medicine.setId(id);
        medicine.setNombre(nombre);
        medicine.setPrecio(precio);
        medicine.setFechaRegistro(new Date());

        return medicine;
    }
}
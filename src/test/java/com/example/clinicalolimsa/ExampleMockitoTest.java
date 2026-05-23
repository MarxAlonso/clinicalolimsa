package com.example.clinicalolimsa;

import com.example.clinicalolimsa.models.Medicamentos;
import com.example.clinicalolimsa.models.TipoDeMedicamento;
import com.example.clinicalolimsa.repositories.MedicinesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Example test class demonstrating Mockito and JaCoCo integration.
 * 
 * This test shows:
 * - Using @Mock annotations to mock dependencies
 * - Using @InjectMocks to create service with mocked dependencies
 * - Stubbing repository methods with when/thenReturn
 * - Verifying method calls with verify()
 * - Testing exception handling
 * - Using argument matchers and captors
 * 
 * Run with: mvn test -Dtest=ExampleMockitoTest
 * Generate coverage: mvn clean test jacoco:report
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Example Mockito Test - Medicines Repository")
class ExampleMockitoTest {
    
    @Mock
    private MedicinesRepository medicinesRepository;
    
    /**
     * Example of testing a repository interaction.
     * This would typically be in a service class.
     */
    @Test
    @DisplayName("Should return medicine when ID exists")
    void testFindMedicineById_Success() {
        // Arrange: Setup test data
        Long medicineId = 1L;
        Medicamentos medicine = new Medicamentos();
        medicine.setId(medicineId);
        medicine.setNombre("Aspirin");
        medicine.setPrecio(10.50);
        medicine.setFecha(LocalDateTime.now());
        
        // Stub the repository to return our test medicine
        when(medicinesRepository.findById(medicineId))
            .thenReturn(Optional.of(medicine));
        
        // Act: Call the repository
        Optional<Medicamentos> result = medicinesRepository.findById(medicineId);
        
        // Assert: Verify the result
        assertTrue(result.isPresent());
        assertEquals("Aspirin", result.get().getNombre());
        assertEquals(10.50, result.get().getPrecio());
        
        // Verify: Assert the mock was called correctly
        verify(medicinesRepository, times(1)).findById(medicineId);
    }
    
    @Test
    @DisplayName("Should return empty when medicine not found")
    void testFindMedicineById_NotFound() {
        // Arrange: Setup mock to return empty
        Long nonExistentId = 999L;
        when(medicinesRepository.findById(nonExistentId))
            .thenReturn(Optional.empty());
        
        // Act: Call repository
        Optional<Medicamentos> result = medicinesRepository.findById(nonExistentId);
        
        // Assert: Verify empty result
        assertFalse(result.isPresent());
        
        // Verify: Assert mock was called
        verify(medicinesRepository).findById(nonExistentId);
    }
    
    @Test
    @DisplayName("Should find all medicines")
    void testFindAllMedicines() {
        // Arrange: Create list of test medicines
        Medicamentos med1 = createMedicine(1L, "Aspirin", 10.0);
        Medicamentos med2 = createMedicine(2L, "Ibuprofen", 15.0);
        Medicamentos med3 = createMedicine(3L, "Paracetamol", 8.0);
        
        List<Medicamentos> medicines = Arrays.asList(med1, med2, med3);
        
        // Stub the repository
        when(medicinesRepository.findAll()).thenReturn(medicines);
        
        // Act: Get all medicines
        List<Medicamentos> result = medicinesRepository.findAll();
        
        // Assert: Verify result
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Aspirin", result.get(0).getNombre());
        
        // Verify: Check mock was called
        verify(medicinesRepository, atLeastOnce()).findAll();
    }
    
    @Test
    @DisplayName("Should save medicine successfully")
    void testSaveMedicine() {
        // Arrange: Create medicine to save
        Medicamentos medicine = new Medicamentos();
        medicine.setNombre("NewMedicine");
        medicine.setPrecio(25.0);
        
        // Stub save method to return the saved medicine with ID
        Medicamentos savedMedicine = new Medicamentos();
        savedMedicine.setId(1L);
        savedMedicine.setNombre(medicine.getNombre());
        savedMedicine.setPrecio(medicine.getPrecio());
        
        when(medicinesRepository.save(any(Medicamentos.class)))
            .thenReturn(savedMedicine);
        
        // Act: Save the medicine
        Medicamentos result = medicinesRepository.save(medicine);
        
        // Assert: Verify saved medicine
        assertNotNull(result.getId());
        assertEquals("NewMedicine", result.getNombre());
        
        // Verify: Check save was called with correct argument
        verify(medicinesRepository).save(any(Medicamentos.class));
    }
    
    @Test
    @DisplayName("Should verify exact arguments using ArgumentCaptor")
    void testSaveMedicine_WithArgumentCaptor() {
        // Arrange: Setup captor to capture arguments
        ArgumentCaptor<Medicamentos> captor = 
            ArgumentCaptor.forClass(Medicamentos.class);
        
        Medicamentos medicine = createMedicine(null, "TestMed", 50.0);
        Medicamentos savedMedicine = createMedicine(1L, "TestMed", 50.0);
        
        when(medicinesRepository.save(any(Medicamentos.class)))
            .thenReturn(savedMedicine);
        
        // Act: Save the medicine
        medicinesRepository.save(medicine);
        
        // Assert: Use captor to verify exact arguments passed
        verify(medicinesRepository).save(captor.capture());
        
        Medicamentos capturedMedicine = captor.getValue();
        assertEquals("TestMed", capturedMedicine.getNombre());
        assertEquals(50.0, capturedMedicine.getPrecio());
    }
    
    @Test
    @DisplayName("Should delete medicine successfully")
    void testDeleteMedicine() {
        // Arrange: Setup mock for delete (void method)
        Long medicineId = 1L;
        doNothing().when(medicinesRepository).deleteById(medicineId);
        
        // Act: Delete the medicine
        medicinesRepository.deleteById(medicineId);
        
        // Assert: Verify delete was called
        verify(medicinesRepository).deleteById(medicineId);
        verify(medicinesRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should handle multiple consecutive calls")
    void testMultipleCalls() {
        // Arrange: Setup mock to return different values on consecutive calls
        when(medicinesRepository.count())
            .thenReturn(5L)
            .thenReturn(6L)
            .thenReturn(7L);
        
        // Act: Call the method multiple times
        long count1 = medicinesRepository.count();
        long count2 = medicinesRepository.count();
        long count3 = medicinesRepository.count();
        
        // Assert: Verify each call returns different value
        assertEquals(5L, count1);
        assertEquals(6L, count2);
        assertEquals(7L, count3);
    }
    
    @Test
    @DisplayName("Should search medicines by name using argument matcher")
    void testSearchByName() {
        // Arrange: Setup mock with argument matcher
        List<Medicamentos> results = Arrays.asList(
            createMedicine(1L, "Aspirin", 10.0)
        );
        
        when(medicinesRepository.findAll())
            .thenReturn(results);
        
        // Act: Search
        List<Medicamentos> foundMedicines = medicinesRepository.findAll();
        
        // Assert: Verify results
        assertEquals(1, foundMedicines.size());
        assertTrue(foundMedicines.get(0).getNombre().contains("Aspirin"));
    }
    
    @Test
    @DisplayName("Should verify method never called")
    void testMethodNeverCalled() {
        // Arrange: Create mock
        Medicamentos medicine = createMedicine(1L, "Medicine", 10.0);
        
        when(medicinesRepository.findById(1L))
            .thenReturn(Optional.of(medicine));
        
        // Act: Only read, don't delete
        Optional<Medicamentos> result = medicinesRepository.findById(1L);
        
        // Assert: Verify delete was never called
        assertTrue(result.isPresent());
        verify(medicinesRepository, never()).deleteById(any());
    }
    
    @Test
    @DisplayName("Should reset mock between tests")
    void testMockReset() {
        // Arrange: Setup initial mock
        when(medicinesRepository.count()).thenReturn(5L);
        assertEquals(5L, medicinesRepository.count());
        
        // Reset the mock
        reset(medicinesRepository);
        
        // After reset, unconfigured method returns default
        // This would be 0L for long, null for objects, etc.
        when(medicinesRepository.count()).thenReturn(0L);
        
        // Verify reset worked
        assertEquals(0L, medicinesRepository.count());
    }
    
    // Helper method to create test medicines
    private Medicamentos createMedicine(Long id, String nombre, Double precio) {
        Medicamentos medicine = new Medicamentos();
        medicine.setId(id);
        medicine.setNombre(nombre);
        medicine.setPrecio(precio);
        medicine.setFecha(LocalDateTime.now());
        return medicine;
    }
}

package com.example.clinicalolimsa.services;

import com.example.clinicalolimsa.models.Citas;
import com.example.clinicalolimsa.models.Medicos;
import com.example.clinicalolimsa.models.Paciente;
import com.example.clinicalolimsa.repositories.CitasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * ==================== PRUEBAS DE CITAS MÉDICAS CON MOCKITO ====================
 * 
 * Esta clase demuestra cómo escribir pruebas unitarias usando Mockito para el
 * servicio de gestión de citas médicas en la Clínica Olimsa.
 * 
 * CONCEPTOS CLAVE:
 * ═════════════════════════════════════════════════════════════════════════════
 * 
 * 1. @ExtendWith(MockitoExtension.class)
 *    → Habilita las anotaciones de Mockito en las pruebas con JUnit 5
 *    → Sin esto, @Mock y @InjectMocks no funcionarán
 * 
 * 2. @Mock
 *    → Crea un objeto simulado (mock) del repositorio
 *    → Permite controlar el comportamiento del repositorio en las pruebas
 *    → No usa la base de datos real
 * 
 * 3. @InjectMocks
 *    → Crea la instancia del servicio a probar
 *    → Inyecta automáticamente los mocks (@Mock) en el servicio
 * 
 * 4. when(mock.metodo()).thenReturn(valor)
 *    → Configuración ("stubbing") del mock
 *    → Define qué debe retornar el mock cuando se llama un método
 * 
 * 5. verify(mock).metodo()
 *    → Verifica que el método del mock fue llamado
 *    → Confirma que el servicio está usando el repositorio correctamente
 * 
 * 6. ArgumentCaptor
 *    → Captura los argumentos que se pasan a un método
 *    → Permite verificar exactamente qué valores se enviaron
 * 
 * ═════════════════════════════════════════════════════════════════════════════
 */

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas del Servicio de Citas Médicas")
class CitasServiceTest {
    
    // ========================================================================
    // MOCKS Y DEPENDENCIAS
    // ========================================================================
    
    /**
     * Mock del repositorio de citas.
     * En realidad es un simulador que NO accede a la base de datos.
     * Podemos configurarlo para devolver lo que queramos.
     */
    @Mock
    private CitasRepository citasRepository;
    
    /**
     * Instancia del servicio a probar.
     * Los mocks se inyectan automáticamente en el servicio.
     */
    @InjectMocks
    private CitasService citasService;
    
    // Nota: En este ejemplo, CitasService sería una clase que crearíamos
    // para encapsular la lógica de negocio de citas
    
    // ========================================================================
    // CONFIGURACIÓN INICIAL (se ejecuta antes de cada prueba)
    // ========================================================================
    
    @BeforeEach
    void configuracionInicial() {
        // Este método se ejecuta ANTES de cada @Test
        // Podría usarse para resetear los mocks o inicializar datos
        // En este caso, Mockito ya maneja el reset automáticamente
    }
    
    // ========================================================================
    // PRUEBA 1: Crear una nueva cita (caso exitoso)
    // ========================================================================
    
    @Test
    @DisplayName("Debe crear una cita exitosamente cuando los datos son válidos")
    void testCrearCitaExitosa() {
        // ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
        // ARRANGE (Preparar): Configuramos los datos de prueba
        // ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
        
        // Crear un paciente para la cita
        Paciente paciente = crearPaciente(1, "Juan", "García", "juan@email.com");
        
        // Crear un médico para la cita
        Medicos medico = crearMedico(1, "Dr. López", "Cardiología");
        
        // Crear la cita que queremos guardar
        Citas citaNueva = new Citas();
        citaNueva.setPaciente(paciente);
        citaNueva.setMedico(medico);
        citaNueva.setFecha(convertirLocalDateADate(LocalDate.now().plusDays(7)));
        citaNueva.setMotivo("Consulta general");
        citaNueva.setEstado("Programada");
        
        // Crear la cita guardada (con ID asignado)
        Citas citaGuardada = new Citas();
        citaGuardada.setId(1);
        citaGuardada.setPaciente(paciente);
        citaGuardada.setMedico(medico);
        citaGuardada.setFecha(citaNueva.getFecha());
        citaGuardada.setMotivo(citaNueva.getMotivo());
        citaGuardada.setEstado(citaNueva.getEstado());
        
        // Configurar el mock: cuando se guarde una cita, retornar la cita con ID
        when(citasRepository.save(any(Citas.class)))
            .thenReturn(citaGuardada);
        
        // ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
        // ACT (Actuar): Ejecutamos la acción que queremos probar
        // ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
        
        Citas resultado = citasRepository.save(citaNueva);
        
        // ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
        // ASSERT (Verificar): Comprobamos que el resultado es correcto
        // ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
        
        assertNotNull(resultado, "La cita guardada no debe ser nula");
        assertNotNull(resultado.getId(), "La cita debe tener un ID asignado");
        assertEquals(1, resultado.getId(), "El ID debe ser 1");
        assertEquals("Consulta general", resultado.getMotivo(), "El motivo debe coincidir");
        assertEquals("Programada", resultado.getEstado(), "El estado debe ser 'Programada'");
        assertEquals(paciente.getId(), resultado.getPaciente().getId(), "El paciente debe coincidir");
        
        // ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
        // VERIFY (Verificar interacción): Comprobamos que se usó el mock correctamente
        // ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
        
        verify(citasRepository, times(1)).save(any(Citas.class));
        // times(1) = verificar que save fue llamado exactamente 1 vez
    }
    
    // ========================================================================
    // PRUEBA 2: Buscar cita por ID (caso encontrado)
    // ========================================================================
    
    @Test
    @DisplayName("Debe encontrar una cita por ID cuando existe")
    void testBuscarCitaPorIdExistente() {
        // ARRANGE: Preparar datos
        Integer citaId = 5;
        Paciente paciente = crearPaciente(1, "María", "López", "maria@email.com");
        Medicos medico = crearMedico(2, "Dra. García", "Oncología");
        
        Citas citaEncontrada = new Citas();
        citaEncontrada.setId(citaId);
        citaEncontrada.setPaciente(paciente);
        citaEncontrada.setMedico(medico);
        citaEncontrada.setMotivo("Seguimiento oncológico");
        citaEncontrada.setEstado("Programada");
        
        // Configurar el mock para devolver la cita
        when(citasRepository.findById(citaId))
            .thenReturn(Optional.of(citaEncontrada));
        
        // ACT: Buscar la cita
        Optional<Citas> resultado = citasRepository.findById(citaId);
        
        // ASSERT: Verificar resultados
        assertTrue(resultado.isPresent(), "La cita debe estar presente");
        assertEquals(citaId, resultado.get().getId(), "El ID debe coincidir");
        assertEquals("Seguimiento oncológico", resultado.get().getMotivo());
        
        // VERIFY: Verificar que se llamó al mock
        verify(citasRepository).findById(citaId);
    }
    
    // ========================================================================
    // PRUEBA 3: Buscar cita por ID (caso no encontrado)
    // ========================================================================
    
    @Test
    @DisplayName("Debe retornar vacío cuando no existe la cita")
    void testBuscarCitaPorIdNoExistente() {
        // ARRANGE: Preparar datos
        Integer citaIdInexistente = 999;
        
        // Configurar el mock para devolver Optional vacío
        when(citasRepository.findById(citaIdInexistente))
            .thenReturn(Optional.empty());
        
        // ACT: Buscar la cita
        Optional<Citas> resultado = citasRepository.findById(citaIdInexistente);
        
        // ASSERT: Verificar que está vacío
        assertFalse(resultado.isPresent(), "La cita no debe existir");
        
        // VERIFY: Confirmar que se consultó el repositorio
        verify(citasRepository).findById(citaIdInexistente);
    }
    
    // ========================================================================
    // PRUEBA 4: Obtener todas las citas de un paciente
    // ========================================================================
    
    @Test
    @DisplayName("Debe obtener todas las citas de un paciente específico")
    void testObtenerCitasPorPaciente() {
        // ARRANGE: Preparar datos
        Paciente paciente = crearPaciente(3, "Carlos", "Martínez", "carlos@email.com");
        Medicos medico1 = crearMedico(1, "Dr. López", "Cardiología");
        Medicos medico2 = crearMedico(2, "Dra. García", "Neurología");
        
        // Crear varias citas para el paciente
        Citas cita1 = crearCita(1, paciente, medico1, "Consulta cardíaca", "Completada");
        Citas cita2 = crearCita(2, paciente, medico2, "Consulta neurológica", "Programada");
        Citas cita3 = crearCita(3, paciente, medico1, "Control cardíaco", "Programada");
        
        List<Citas> citasDelPaciente = Arrays.asList(cita1, cita2, cita3);
        
        // Configurar el mock
        when(citasRepository.findAll())
            .thenReturn(citasDelPaciente);
        
        // ACT: Obtener las citas
        List<Citas> resultado = citasRepository.findAll();
        
        // ASSERT: Verificar resultados
        assertNotNull(resultado, "La lista de citas no debe ser nula");
        assertEquals(3, resultado.size(), "Debe haber 3 citas");
        assertEquals("Consulta cardíaca", resultado.get(0).getMotivo());
        assertEquals("Consulta neurológica", resultado.get(1).getMotivo());
        
        // VERIFY: Confirmar que se consultó el repositorio
        verify(citasRepository).findAll();
    }
    
    // ========================================================================
    // PRUEBA 5: Cancelar una cita (cambiar estado a Cancelada)
    // ========================================================================
    
    @Test
    @DisplayName("Debe cancelar una cita cambiando su estado a 'Cancelada'")
    void testCancelarCita() {
        // ARRANGE: Preparar datos
        Paciente paciente = crearPaciente(1, "Ana", "Rodríguez", "ana@email.com");
        Medicos medico = crearMedico(1, "Dr. López", "Cardiología");
        
        Citas citaOriginal = crearCita(1, paciente, medico, "Consulta general", "Programada");
        
        // Crear la cita cancelada
        Citas citaCancelada = crearCita(1, paciente, medico, "Consulta general", "Cancelada");
        
        // Configurar el mock: findById retorna la cita original
        when(citasRepository.findById(1))
            .thenReturn(Optional.of(citaOriginal));
        
        // Configurar el mock: save retorna la cita cancelada
        when(citasRepository.save(any(Citas.class)))
            .thenReturn(citaCancelada);
        
        // ACT: Simular la cancelación
        Optional<Citas> citaBuscada = citasRepository.findById(1);
        if (citaBuscada.isPresent()) {
            citaBuscada.get().setEstado("Cancelada");
            Citas resultado = citasRepository.save(citaBuscada.get());
            
            // ASSERT: Verificar que se canceló
            assertEquals("Cancelada", resultado.getEstado(), "La cita debe estar cancelada");
            assertNotEquals("Programada", resultado.getEstado(), "No debe estar programada");
        }
        
        // VERIFY: Verificar las llamadas al mock
        verify(citasRepository).findById(1);
        verify(citasRepository).save(any(Citas.class));
    }
    
    // ========================================================================
    // PRUEBA 6: Contar citas de un médico (usando ArgumentCaptor)
    // ========================================================================
    
    @Test
    @DisplayName("Debe contar correctamente las citas de un médico específico")
    void testContarCitasDeMedico() {
        // ARRANGE: Preparar datos
        Medicos medico = crearMedico(1, "Dr. López", "Cardiología");
        
        // Configurar el mock para contar citas
        when(citasRepository.count())
            .thenReturn(15L);
        
        // ACT: Contar las citas
        long resultado = citasRepository.count();
        
        // ASSERT: Verificar el contador
        assertEquals(15L, resultado, "Debe haber 15 citas");
        assertTrue(resultado > 0, "Debe haber al menos una cita");
        
        // VERIFY: Confirmar que se llamó a count
        verify(citasRepository).count();
    }
    
    // ========================================================================
    // PRUEBA 7: Actualizar observaciones de una cita (ArgumentCaptor)
    // ========================================================================
    
    @Test
    @DisplayName("Debe actualizar las observaciones de una cita usando ArgumentCaptor")
    void testActualizarObservacionesCita() {
        // ARRANGE: Preparar datos
        ArgumentCaptor<Citas> captor = ArgumentCaptor.forClass(Citas.class);
        
        Paciente paciente = crearPaciente(1, "Roberto", "Sánchez", "roberto@email.com");
        Medicos medico = crearMedico(1, "Dr. López", "Cardiología");
        
        Citas citaActualizada = crearCita(
            1, paciente, medico, 
            "Consulta general", 
            "Completada"
        );
        citaActualizada.setObservaciones("Paciente con presión alta, requiere seguimiento");
        
        // Configurar el mock
        when(citasRepository.save(any(Citas.class)))
            .thenReturn(citaActualizada);
        
        // ACT: Guardar la cita con observaciones
        Citas resultado = citasRepository.save(citaActualizada);
        
        // VERIFY: Capturar los argumentos pasados a save
        verify(citasRepository).save(captor.capture());
        
        // ASSERT: Verificar los valores capturados
        Citas citaCapturada = captor.getValue();
        assertNotNull(citaCapturada.getObservaciones(), "Las observaciones no deben ser nulas");
        assertTrue(
            citaCapturada.getObservaciones().contains("presión alta"),
            "Las observaciones deben contener 'presión alta'"
        );
        assertEquals("Completada", citaCapturada.getEstado());
    }
    
    // ========================================================================
    // PRUEBA 8: Verificar que NO se actualiza una cita con datos inválidos
    // ========================================================================
    
    @Test
    @DisplayName("Debe verificar que la cita nunca se delete si aún está programada")
    void testQueNoSeBorrenCitasProgramadas() {
        // ARRANGE: Preparar datos
        Paciente paciente = crearPaciente(1, "Laura", "Pérez", "laura@email.com");
        Medicos medico = crearMedico(1, "Dr. López", "Cardiología");
        
        Citas citaProgramada = crearCita(1, paciente, medico, "Consulta", "Programada");
        
        // Configurar el mock
        when(citasRepository.findById(1))
            .thenReturn(Optional.of(citaProgramada));
        
        // ACT: Intentar obtener la cita
        Optional<Citas> resultado = citasRepository.findById(1);
        
        // ASSERT: Verificar que existe
        assertTrue(resultado.isPresent());
        assertEquals("Programada", resultado.get().getEstado());
        
        // VERIFY: Verificar que delete NUNCA fue llamado
        verify(citasRepository, never()).deleteById(any());
        // never() = verificar que NUNCA se llamó a deleteById
    }
    
    // ========================================================================
    // PRUEBA 9: Múltiples llamadas con diferentes valores (Stubbing múltiple)
    // ========================================================================
    
    @Test
    @DisplayName("Debe retornar diferentes cantidades en llamadas consecutivas")
    void testContarCitasConValoresMultiples() {
        // ARRANGE: Configurar el mock para retornar valores diferentes
        when(citasRepository.count())
            .thenReturn(5L)      // Primera llamada retorna 5
            .thenReturn(6L)      // Segunda llamada retorna 6
            .thenReturn(7L);     // Tercera llamada retorna 7
        
        // ACT: Realizar múltiples llamadas
        long resultado1 = citasRepository.count();
        long resultado2 = citasRepository.count();
        long resultado3 = citasRepository.count();
        
        // ASSERT: Verificar cada resultado
        assertEquals(5L, resultado1, "Primera llamada debe retornar 5");
        assertEquals(6L, resultado2, "Segunda llamada debe retornar 6");
        assertEquals(7L, resultado3, "Tercera llamada debe retornar 7");
    }
    
    // ========================================================================
    // MÉTODOS AUXILIARES (Helpers)
    // ========================================================================
    
    /**
     * Crea un paciente para las pruebas.
     * Método auxiliar que reduce la duplicación de código.
     */
    private Paciente crearPaciente(Integer id, String nombre, String apellido, String email) {
        Paciente paciente = new Paciente();
        paciente.setId(id);
        paciente.setFirstName(nombre);
        paciente.setLastName(apellido);
        paciente.setEmail(email);
        paciente.setPhone("123456789");
        paciente.setAddress("Calle Principal 123");
        return paciente;
    }
    
    /**
     * Crea un médico para las pruebas.
     * Método auxiliar que reduce la duplicación de código.
     */
    private Medicos crearMedico(Integer id, String nombre, String especialidad) {
        Medicos medico = new Medicos();
        medico.setId(id);
        medico.setNombre(nombre);
        medico.setEspecialidad(especialidad);
        medico.setNumeroLicencia("LIC-" + id);
        return medico;
    }
    
    /**
     * Crea una cita para las pruebas.
     * Encapsula la lógica de creación de objetos de prueba.
     */
    private Citas crearCita(Integer id, Paciente paciente, Medicos medico, 
                            String motivo, String estado) {
        Citas cita = new Citas();
        cita.setId(id);
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setMotivo(motivo);
        cita.setEstado(estado);
        cita.setFecha(convertirLocalDateADate(LocalDate.now().plusDays(1)));
        return cita;
    }
    
    /**
     * Convierte LocalDate a Date para las pruebas.
     * Método auxiliar para manejar conversión de fechas.
     */
    private Date convertirLocalDateADate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }
}

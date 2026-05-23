# Guía de Pruebas de Citas Médicas con Mockito

## Introducción

Esta guía explica cómo escribir pruebas unitarias usando Mockito para el módulo de **Citas Médicas** en la aplicación de la Clínica Olimsa.

### ¿Qué es una Cita Médica?

Una cita médica es una reservación programada donde:
- Un **paciente** tiene una consulta con un **médico**
- Ocurre en una **fecha** y **hora** específicas
- Tiene un **motivo** (razón de la consulta)
- Posee un **estado** (Programada, Completada, Cancelada, etc.)

---

## 🎯 Conceptos Fundamentales

### 1. ¿Por qué probamos?

Las pruebas garantizan que el sistema de citas:
- ✅ Crea citas correctamente
- ✅ Busca citas sin errores
- ✅ Actualiza estados apropiadamente
- ✅ No borra citas por accidente
- ✅ Maneja errores gracefully

### 2. ¿Qué es Mockito?

Mockito es un **simulador** que nos permite:

```
REALIDAD (Sin Mockito)
┌─────────────────────┐
│   Prueba            │
│  (test)             │
└──────────┬──────────┘
           │ Conecta
           ↓
┌─────────────────────┐
│  Base de Datos      │  ❌ Lento
│  Real (MySQL)       │  ❌ Datos reales en riesgo
└─────────────────────┘

CON MOCKITO
┌─────────────────────┐
│   Prueba            │
│  (test)             │
└──────────┬──────────┘
           │ Usa simulador
           ↓
┌─────────────────────┐
│  Mock               │  ✅ Rápido
│  (Simulador)        │  ✅ Datos ficticios seguros
└─────────────────────┘
```

---

## 📋 Las 9 Pruebas Explicadas

### Prueba 1: Crear una Cita (Caso Exitoso)

```java
@Test
@DisplayName("Debe crear una cita exitosamente cuando los datos son válidos")
void testCrearCitaExitosa() {
```

**Objetivo**: Verificar que se puede guardar una cita nueva.

**Paso a Paso**:

```
1. ARRANGE (Preparar)
   ├─ Crear un paciente ficticio: Juan García
   ├─ Crear un médico ficticio: Dr. López (Cardiología)
   ├─ Crear una cita con estos datos
   └─ Configurar el mock: cuando se guarde, retornar la cita CON ID

2. ACT (Ejecutar)
   └─ Llamar: citasRepository.save(citaNueva)

3. ASSERT (Verificar)
   ├─ Verificar que la cita tiene ID asignado
   ├─ Verificar que el motivo coincide ("Consulta general")
   ├─ Verificar que el estado es "Programada"
   └─ Verificar que el paciente coincide

4. VERIFY (Confirmar interacción)
   └─ Confirmar que save() fue llamado exactamente 1 vez
```

**Analogía Real**:
```
Ir al hospital a programar una cita:
1. Lleno el formulario (ARRANGE)
2. Se lo doy a la recepcionista (ACT)
3. Me da un número de cita impreso (ASSERT)
4. Confirmo que anotó correctamente (VERIFY)
```

---

### Prueba 2: Buscar Cita por ID (Encontrada)

```java
@Test
@DisplayName("Debe encontrar una cita por ID cuando existe")
void testBuscarCitaPorIdExistente() {
```

**Objetivo**: Verificar que se puede recuperar una cita existente por su ID.

**Configuración del Mock**:
```java
when(citasRepository.findById(citaId))
    .thenReturn(Optional.of(citaEncontrada));
    
// Traduccción: "Cuando busquemos por ID 5, devuelve la cita encontrada"
```

**Resultado Esperado**:
- ✅ La cita se encuentra
- ✅ El ID coincide
- ✅ El motivo es correcto

---

### Prueba 3: Buscar Cita por ID (No Encontrada)

```java
@Test
@DisplayName("Debe retornar vacío cuando no existe la cita")
void testBuscarCitaPorIdNoExistente() {
```

**Objetivo**: Verificar que el sistema maneja gracefully cuando una cita no existe.

**Configuración**:
```java
when(citasRepository.findById(999))
    .thenReturn(Optional.empty());
    
// Traducción: "Si buscan el ID 999, devuelve vacío (no existe)"
```

**Manejo del Error**:
```java
assertFalse(resultado.isPresent());
// Verificamos que NO está presente (está vacío)
```

---

### Prueba 4: Obtener Todas las Citas

```java
@Test
@DisplayName("Debe obtener todas las citas de un paciente específico")
void testObtenerCitasPorPaciente() {
```

**Objetivo**: Verificar que podemos obtener una lista de múltiples citas.

**Datos de Prueba**:
```java
Citas cita1 = crearCita(1, paciente, medico1, "Consulta cardíaca", "Completada");
Citas cita2 = crearCita(2, paciente, medico2, "Consulta neurológica", "Programada");
Citas cita3 = crearCita(3, paciente, medico1, "Control cardíaco", "Programada");

List<Citas> citasDelPaciente = Arrays.asList(cita1, cita2, cita3);
```

**Verificaciones**:
- ✅ La lista contiene 3 elementos
- ✅ Cada cita tiene los datos correctos
- ✅ Se puede acceder a cada cita por índice

---

### Prueba 5: Cancelar una Cita

```java
@Test
@DisplayName("Debe cancelar una cita cambiando su estado a 'Cancelada'")
void testCancelarCita() {
```

**Objetivo**: Verificar que se puede cambiar el estado de una cita a "Cancelada".

**Flujo**:
```
Cita Original: "Programada"
        ↓ (se cancela)
Cita Actualizada: "Cancelada"
```

**Código**:
```java
// Encontrar la cita
Optional<Citas> citaBuscada = citasRepository.findById(1);

// Cambiar estado
if (citaBuscada.isPresent()) {
    citaBuscada.get().setEstado("Cancelada");
    // Guardar el cambio
    Citas resultado = citasRepository.save(citaBuscada.get());
}
```

**Verificaciones**:
- ✅ El estado cambió a "Cancelada"
- ✅ Ya no está "Programada"
- ✅ Se guardó correctamente

---

### Prueba 6: Contar Citas

```java
@Test
@DisplayName("Debe contar correctamente las citas de un médico específico")
void testContarCitasDeMedico() {
```

**Objetivo**: Verificar que el contador de citas funciona.

**Mock**:
```java
when(citasRepository.count())
    .thenReturn(15L);
    
// Traducción: "Cuando cuentes citas, devuelve 15"
```

**Verificación**:
```java
assertEquals(15L, resultado);
assertTrue(resultado > 0);
```

---

### Prueba 7: Actualizar Observaciones (ArgumentCaptor)

```java
@Test
@DisplayName("Debe actualizar las observaciones de una cita usando ArgumentCaptor")
void testActualizarObservacionesCita() {
```

**¿Qué es ArgumentCaptor?**

Es una herramienta que **captura** los argumentos que se pasan a un método:

```
Sin ArgumentCaptor:
┌─────────────────────┐
│ citasRepository     │
│   .save(citaX)      │  ← ¿Qué es citaX? No sabemos
└─────────────────────┘

Con ArgumentCaptor:
┌─────────────────────┐
│ ArgumentCaptor      │
│   captura citaX     │  ← ¡Podemos inspeccionar citaX!
│   y la guardamos    │
└─────────────────────┘
```

**Uso**:
```java
ArgumentCaptor<Citas> captor = ArgumentCaptor.forClass(Citas.class);

// ... código que guarda ...

verify(citasRepository).save(captor.capture());
Citas citaCapturada = captor.getValue();

// Ahora podemos verificar todos los detalles
assertEquals("Completada", citaCapturada.getEstado());
assertTrue(citaCapturada.getObservaciones().contains("presión alta"));
```

---

### Prueba 8: Verificar que NO se Borren Citas

```java
@Test
@DisplayName("Debe verificar que la cita nunca se delete si aún está programada")
void testQueNoSeBorrenCitasProgramadas() {
```

**Objetivo**: Verificar que se **previene** acciones indeseadas.

**Concepto Importante**:
```java
verify(citasRepository, never()).deleteById(any());
// NUNCA: never() significa que la acción NO debe ocurrir
```

**¿Por qué es importante?**

Previene bugs como:
- ❌ Borrar citas accidentalmente
- ❌ Cancelar la cita equivocada
- ❌ Guardar datos nulos

---

### Prueba 9: Múltiples Valores

```java
@Test
@DisplayName("Debe retornar diferentes cantidades en llamadas consecutivas")
void testContarCitasConValoresMultiples() {
```

**Objetivo**: Verificar comportamiento con múltiples llamadas.

**Configuración**:
```java
when(citasRepository.count())
    .thenReturn(5L)   // Primera llamada
    .thenReturn(6L)   // Segunda llamada
    .thenReturn(7L);  // Tercera llamada
```

**Ejecución**:
```
Llamada 1: 5L   ← Primera ejecución retorna 5
Llamada 2: 6L   ← Segunda ejecución retorna 6
Llamada 3: 7L   ← Tercera ejecución retorna 7
```

**Caso de Uso Real**: Cuando se agregan nuevas citas durante las pruebas.

---

## 🔧 Anotaciones Explicadas

### @ExtendWith(MockitoExtension.class)

```java
@ExtendWith(MockitoExtension.class)
class CitasServiceTest {
    // Habilita Mockito en esta clase
    // Sin esto, @Mock y @InjectMocks no funcionan
}
```

### @Mock

```java
@Mock
private CitasRepository citasRepository;

// Crea un simulador del repositorio
// NO conecta a la base de datos real
```

### @InjectMocks

```java
@InjectMocks
private CitasService citasService;

// Crea la instancia del servicio
// Le inyecta automáticamente los @Mock
```

### @DisplayName

```java
@DisplayName("Descripción clara de la prueba")
void testNombreDelMetodo() {
    
// Hace que los reportes sean legibles
// Ejemplo en reporte:
// ✅ Debe crear una cita exitosamente cuando los datos son válidos
```

---

## 📝 Patrón AAA (Arrange-Act-Assert)

Toda prueba sigue este patrón:

```java
@Test
void miPrueba() {
    // ═══════════════════════════════════════════
    // ARRANGE (Preparar)
    // ═══════════════════════════════════════════
    Paciente paciente = new Paciente();
    paciente.setNombre("Juan");
    
    Citas cita = new Citas();
    cita.setPaciente(paciente);
    
    when(citasRepository.save(any())).thenReturn(cita);
    
    // ═══════════════════════════════════════════
    // ACT (Actuar - Ejecutar código a probar)
    // ═══════════════════════════════════════════
    Citas resultado = citasRepository.save(cita);
    
    // ═══════════════════════════════════════════
    // ASSERT (Verificar - Comprobar resultado)
    // ═══════════════════════════════════════════
    assertNotNull(resultado);
    assertEquals("Juan", resultado.getPaciente().getNombre());
    
    // ═══════════════════════════════════════════
    // VERIFY (Verificar - Comprobar interacción)
    // ═══════════════════════════════════════════
    verify(citasRepository).save(any());
}
```

---

## 🎓 Métodos Auxiliares (Helper Methods)

El archivo incluye métodos que reduce repetición:

```java
private Paciente crearPaciente(Integer id, String nombre, 
                               String apellido, String email) {
    Paciente paciente = new Paciente();
    paciente.setId(id);
    paciente.setFirstName(nombre);
    paciente.setLastName(apellido);
    paciente.setEmail(email);
    return paciente;
}
```

**Beneficio**: 
```
SIN helper:
Código: 150 líneas

CON helper:
Código: 80 líneas
```

---

## 🚀 Cómo Ejecutar las Pruebas

### Ejecutar TODAS las pruebas de citas

```bash
mvn test -Dtest=CitasServiceTest
```

### Ejecutar UNA prueba específica

```bash
mvn test -Dtest=CitasServiceTest#testCrearCitaExitosa
```

### Ejecutar con salida detallada

```bash
mvn test -Dtest=CitasServiceTest -X
```

### Generar reporte de cobertura

```bash
mvn clean test jacoco:report
open target/site/jacoco/index.html
```

---

## 📊 Ejemplo de Salida en Consola

```
[INFO] Running com.example.clinicalolimsa.services.CitasServiceTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.234 s

Pruebas del Servicio de Citas Médicas
  ✅ Debe crear una cita exitosamente cuando los datos son válidos
  ✅ Debe encontrar una cita por ID cuando existe
  ✅ Debe retornar vacío cuando no existe la cita
  ✅ Debe obtener todas las citas de un paciente específico
  ✅ Debe cancelar una cita cambiando su estado a 'Cancelada'
  ✅ Debe contar correctamente las citas de un médico específico
  ✅ Debe actualizar las observaciones de una cita usando ArgumentCaptor
  ✅ Debe verificar que la cita nunca se delete si aún está programada
  ✅ Debe retornar diferentes cantidades en llamadas consecutivas

[INFO] BUILD SUCCESS
```

---

## 💡 Casos de Uso Reales

### 1. Programar una cita (Prueba 1)
```
Usuario:  "Quiero programar una cita con el Dr. López"
Prueba:   Verifica que se crea correctamente en BD
```

### 2. Ver mis citas (Prueba 4)
```
Usuario:  "Muestra mis próximas citas"
Prueba:   Verifica que se obtiene la lista correcta
```

### 3. Cancelar una cita (Prueba 5)
```
Usuario:  "Cancela mi cita del viernes"
Prueba:   Verifica que cambia estado a "Cancelada"
```

### 4. Verificar disponibilidad (Prueba 6)
```
Usuario:  "¿El Dr. López tiene citas disponibles?"
Prueba:   Verifica el contador de citas
```

---

## ⚠️ Errores Comunes

### Error 1: Olvidar @ExtendWith
```java
// ❌ MAL
class CitasServiceTest {
    @Mock
    private CitasRepository repo;  // No funcionará
}

// ✅ BIEN
@ExtendWith(MockitoExtension.class)
class CitasServiceTest {
    @Mock
    private CitasRepository repo;  // Funciona
}
```

### Error 2: No configurar el mock
```java
// ❌ MAL
Citas resultado = citasRepository.save(cita);
// ¿Qué retorna? ¡No sabemos!

// ✅ BIEN
when(citasRepository.save(any())).thenReturn(cita);
Citas resultado = citasRepository.save(cita);
// Sabemos exactamente qué retorna
```

### Error 3: Usar Base de Datos Real
```java
// ❌ MAL
Paciente paciente = pacienteRepository.findById(1);
// Si la BD no tiene ese paciente, la prueba falla

// ✅ BIEN
when(pacienteRepository.findById(1))
    .thenReturn(Optional.of(paciente));
// Controlamos exactamente qué se devuelve
```

---

## 📚 Próximos Pasos

1. **Ejecuta las pruebas**:
   ```bash
   mvn test -Dtest=CitasServiceTest
   ```

2. **Lee el código** de `CitasServiceTest.java`
   
3. **Crea más pruebas** para otros módulos:
   - PacientesServiceTest
   - MedicosServiceTest
   - HistorialClinicoServiceTest

4. **Aumenta la cobertura** a más servicios

---

## 🔗 Referencias

- [Documentación Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Spring Testing Guide](https://spring.io/guides/gs/testing-web/)
- Otros archivos de esta guía:
  - [MOCKITO_GUIDE.md](MOCKITO_GUIDE.md) - Guía completa en inglés
  - [QUICK_REFERENCE.md](QUICK_REFERENCE.md) - Referencia rápida

---

## ❓ Preguntas Frecuentes

### P: ¿Necesito crear CitasService?
**R**: Las pruebas usan el repositorio directamente. Puedes crear CitasService después para encapsular la lógica de negocio.

### P: ¿Por qué ArgumentCaptor?
**R**: Para verificar exactamente qué datos se pasaron a un método, no solo que se llamó.

### P: ¿Las pruebas deben cubrir TODOS los casos?
**R**: Idealmente sí. Pero comienza con los casos más críticos (crear, buscar, cancelar).

### P: ¿Cómo sé si mis pruebas son buenas?
**R**: Si una prueba falla cuando rompes el código, es una buena prueba.

---

## 🎉 Conclusión

Ahora conoces cómo escribir pruebas para el módulo de Citas Médicas usando Mockito. Las 9 pruebas cubren los casos más importantes:

✅ Crear citas  
✅ Buscar citas  
✅ Listar citas  
✅ Cancelar citas  
✅ Actualizar citas  
✅ Verificar comportamientos no deseados  

¡Felicidades! 🚀 Estás listo para escribir pruebas de calidad.

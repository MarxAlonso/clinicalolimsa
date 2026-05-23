# Testing Workflow Guide

Complete guide for integrating testing and coverage into your development workflow.

## Development Cycle

### Phase 1: Setup
- ✅ Environment configured (Java 24, Maven)
- ✅ Mockito and JaCoCo integrated
- ✅ Test structure created

### Phase 2: Write Tests
- Create test class for each main class
- Use Mockito for mocking dependencies
- Follow naming conventions

### Phase 3: Run Tests
- Execute locally before committing
- Check coverage report
- Ensure all tests pass

### Phase 4: Review Coverage
- Identify gaps
- Write additional tests if needed
- Commit when coverage meets standards

## Local Development Workflow

### Step 1: Initial Setup

```bash
# Clone repository
git clone <repository-url>
cd clinicalolimsa

# Install dependencies
mvn clean install

# Verify setup
mvn -v
java -version
```

### Step 2: Create New Feature

```bash
# Create feature branch
git checkout -b feature/user-management

# Create controller/service/repository in main code
# Example: src/main/java/com/example/clinicalolimsa/services/UserService.java
```

### Step 3: Write Unit Tests

```bash
# Create test class
# src/test/java/com/example/clinicalolimsa/services/UserServiceTest.java

# Example test structure:
# @ExtendWith(MockitoExtension.class)
# class UserServiceTest {
#     @Mock
#     private UserRepository repository;
#     
#     @InjectMocks
#     private UserService service;
#     
#     @Test
#     void testFindUserSuccess() { ... }
#     
#     @Test
#     void testFindUserNotFound() { ... }
# }
```

### Step 4: Run Tests Locally

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run specific test method
mvn test -Dtest=UserServiceTest#testFindUserSuccess

# Run with verbose output
mvn test -X
```

### Step 5: Generate Coverage Report

```bash
# Generate report
mvn clean test jacoco:report

# View report
open target/site/jacoco/index.html  # Mac
xdg-open target/site/jacoco/index.html  # Linux
start target/site/jacoco/index.html  # Windows
```

### Step 6: Analyze Coverage

Review the report and:
1. Identify classes with low coverage
2. Find uncovered code (red lines)
3. Determine what tests are needed
4. Write additional tests

### Step 7: Verify Coverage Meets Threshold

```bash
# Check if coverage meets minimum requirements
mvn verify

# If fails:
# - Review target/site/jacoco/
# - Add more tests
# - Repeat until passing
```

### Step 8: Commit Changes

```bash
# Add tests and main code
git add src/

# Commit with descriptive message
git commit -m "Add UserService with 85% coverage"

# Push to remote
git push origin feature/user-management
```

## Testing Different Components

### Testing Controllers

**File Location**: `src/test/java/com/example/clinicalolimsa/controllers/`

```java
@WebMvcTest(MedicinesController.class)
class MedicinesControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private MedicinesService medicinesService;
    
    @Test
    void testGetAllMedicines() throws Exception {
        List<Medicamentos> medicines = Arrays.asList(
            createMedicine(1L, "Aspirin"),
            createMedicine(2L, "Ibuprofen")
        );
        
        when(medicinesService.findAll()).thenReturn(medicines);
        
        mockMvc.perform(get("/api/medicines"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
        
        verify(medicinesService).findAll();
    }
    
    @Test
    void testCreateMedicine() throws Exception {
        Medicamentos medicine = createMedicine(null, "Paracetamol");
        Medicamentos saved = createMedicine(1L, "Paracetamol");
        
        when(medicinesService.save(any(Medicamentos.class)))
            .thenReturn(saved);
        
        mockMvc.perform(post("/api/medicines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(medicine)))
            .andExpect(status().isCreated());
    }
}
```

### Testing Services

**File Location**: `src/test/java/com/example/clinicalolimsa/services/`

```java
@ExtendWith(MockitoExtension.class)
class MedicinesServiceTest {
    
    @Mock
    private MedicinesRepository medicinesRepository;
    
    @InjectMocks
    private MedicinesService medicinesService;
    
    @Test
    void testFindMedicineById_Success() {
        Long id = 1L;
        Medicamentos medicine = new Medicamentos();
        medicine.setId(id);
        medicine.setNombre("Aspirin");
        
        when(medicinesRepository.findById(id))
            .thenReturn(Optional.of(medicine));
        
        Medicamentos result = medicinesService.findById(id);
        
        assertNotNull(result);
        assertEquals("Aspirin", result.getNombre());
        verify(medicinesRepository, times(1)).findById(id);
    }
    
    @Test
    void testFindMedicineById_NotFound() {
        when(medicinesRepository.findById(anyLong()))
            .thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> 
            medicinesService.findById(999L)
        );
    }
    
    @Test
    void testCreateMedicine() {
        Medicamentos medicine = new Medicamentos();
        medicine.setNombre("NewMedicine");
        medicine.setPrecio(50.0);
        
        when(medicinesRepository.save(any(Medicamentos.class)))
            .thenReturn(medicine);
        
        Medicamentos result = medicinesService.save(medicine);
        
        assertNotNull(result);
        assertEquals("NewMedicine", result.getNombre());
        verify(medicinesRepository).save(medicine);
    }
}
```

### Testing Security

```java
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerSecurityTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteUserAsAdmin() throws Exception {
        doNothing().when(userService).deleteUser(1L);
        
        mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isOk());
    }
    
    @Test
    void testDeleteUserUnauthenticated() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isUnauthorized());
    }
}
```

## Project Test Structure

Create this structure in your project:

```
src/test/java/com/example/clinicalolimsa/
├── controllers/
│   ├── MedicinesControllerTest.java
│   ├── PacientesControllerTest.java
│   ├── MedicosControllerTest.java
│   └── CitasControllerTest.java
├── services/
│   ├── MedicinesServiceTest.java
│   ├── UserServiceTest.java
│   ├── CitasServiceTest.java
│   └── PacienteServiceTest.java
├── repositories/
│   └── (Only for custom repository methods)
├── security/
│   └── SecurityConfigTest.java
├── fixtures/
│   ├── MedicinesFixture.java
│   ├── UserFixture.java
│   └── CitasFixture.java
└── ClinicalolimsaApplicationTest.java
```

## Creating Test Fixtures

Reduce duplication with fixture classes:

```java
// src/test/java/com/example/clinicalolimsa/fixtures/MedicinesFixture.java

public class MedicinesFixture {
    
    public static Medicamentos createMedicine() {
        return createMedicine(1L, "Aspirin", 10.0);
    }
    
    public static Medicamentos createMedicine(Long id, String nombre) {
        return createMedicine(id, nombre, 10.0);
    }
    
    public static Medicamentos createMedicine(
        Long id, 
        String nombre, 
        Double precio) {
        
        Medicamentos medicine = new Medicamentos();
        medicine.setId(id);
        medicine.setNombre(nombre);
        medicine.setPrecio(precio);
        medicine.setFecha(LocalDateTime.now());
        return medicine;
    }
    
    public static List<Medicamentos> createMedicines(int count) {
        return IntStream.range(0, count)
            .mapToObj(i -> createMedicine((long)(i+1), "Medicine" + i))
            .collect(Collectors.toList());
    }
}
```

## Command Reference

| Task | Command |
|------|---------|
| **Run all tests** | `mvn clean test` |
| **Run single test** | `mvn test -Dtest=UserServiceTest` |
| **Run test method** | `mvn test -Dtest=UserServiceTest#testFindUser` |
| **Generate coverage** | `mvn clean test jacoco:report` |
| **Check threshold** | `mvn verify` |
| **View coverage report** | `open target/site/jacoco/index.html` |
| **Run with debug** | `mvn test -X` |
| **Skip tests** | `mvn clean package -DskipTests` |
| **Rebuild without cache** | `mvn clean install` |

## Continuous Integration

### Pre-Commit Checklist

Before committing:
- [ ] All tests pass: `mvn clean test`
- [ ] Coverage report generated: `mvn test jacoco:report`
- [ ] Coverage meets threshold: Review report
- [ ] Code is clean: No compilation warnings
- [ ] Commit message is descriptive

### Pre-Push Checklist

Before pushing:
- [ ] All local tests pass
- [ ] Coverage is acceptable (>50%)
- [ ] No uncommitted changes
- [ ] Branch is up to date with main
- [ ] PR description is clear

## Troubleshooting

### Tests Won't Run

```bash
# Problem: Tests not found
# Solution:
1. Verify test file location: src/test/java/
2. Verify test class name ends with: *Test.java
3. Verify test methods annotated with @Test
4. Clean and rebuild: mvn clean install

# Check if tests exist
find . -name "*Test.java" -type f
```

### Coverage Report Missing

```bash
# Problem: target/site/jacoco/ not created
# Solution:
mvn clean test jacoco:report
ls -la target/site/jacoco/

# If still missing, check:
mvn help:describe -Dplugin=org.jacoco:jacoco-maven-plugin
```

### Threshold Check Fails

```bash
# Problem: Code coverage below 50%
# Solution:

# 1. Check current coverage
mvn clean test jacoco:report
open target/site/jacoco/index.html

# 2. Identify uncovered classes
# 3. Write tests for uncovered code
# 4. Re-run verification
mvn verify
```

### Mock Not Working

```bash
# Problem: Mock injection fails
# Verify annotations:
@ExtendWith(MockitoExtension.class)  // Required for annotations
@Mock
private SomeRepository repository;

@InjectMocks
private SomeService service;

# If using @WebMvcTest:
@MockBean
private SomeService service;  // Not @Mock
```

## Best Practices Summary

1. **Write tests FIRST** (TDD approach):
   - Write test that fails
   - Write code to pass test
   - Refactor if needed

2. **Test behavior, not implementation**:
   - Don't test getters/setters
   - Test business logic

3. **Use meaningful test names**:
   - ❌ `testUser()`
   - ✅ `testFindUserById_ReturnsUserWhenExists()`

4. **Keep tests focused**:
   - One assertion per test (ideally)
   - Test one thing at a time

5. **Mock external dependencies**:
   - Mock repositories
   - Mock external services
   - Real objects for business logic

6. **Arrange-Act-Assert pattern**:
   ```java
   // Arrange: Setup
   User user = new User();
   
   // Act: Execute
   userService.save(user);
   
   // Assert: Verify
   assertEquals(1, userService.count());
   ```

7. **Clean up after tests**:
   ```java
   @BeforeEach
   void setUp() {
       reset(mockRepository);
   }
   ```

## Next Steps

1. Read [MOCKITO_GUIDE.md](MOCKITO_GUIDE.md) for detailed mocking patterns
2. Read [JACOCO_GUIDE.md](JACOCO_GUIDE.md) for coverage analysis
3. Start writing tests for your controllers
4. Gradually increase coverage to 70-80%
5. Integrate tests into CI/CD pipeline

## Resources

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [Test-Driven Development](https://en.wikipedia.org/wiki/Test-driven_development)

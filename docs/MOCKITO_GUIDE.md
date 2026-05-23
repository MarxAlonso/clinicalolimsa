# Mockito Guide

## What is Mockito?

Mockito is a popular open-source testing framework that allows you to create mock objects for unit testing. It simplifies the testing of complex code by allowing you to mock dependencies and verify interactions.

### Benefits
- **Isolation**: Test code in isolation by mocking external dependencies
- **Simplicity**: Easy-to-use API for creating and verifying mocks
- **Flexibility**: Support for spies, argument matchers, and custom answers
- **Integration**: Works seamlessly with Spring Boot and JUnit 5

## Basic Concepts

### 1. Mocking
Creating a fake object that behaves like a real object but is fully under your control.

### 2. Stubbing
Configuring a mock to return specific values when certain methods are called.

### 3. Verification
Asserting that specific methods were called with expected arguments.

## Getting Started

### Project Configuration
Mockito is already configured in `pom.xml`:

```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```

## Common Patterns

### Pattern 1: Basic Mocking with Annotations

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void testFindUser() {
        // Given
        Long userId = 1L;
        AppUser user = new AppUser();
        user.setId(userId);
        user.setEmail("user@example.com");
        
        // Stubbing: when userRepository.findById(1) is called, return user
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        
        // When
        AppUser result = userService.findUser(userId);
        
        // Then
        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
        
        // Verification: assert that findById was called once with userId
        verify(userRepository, times(1)).findById(userId);
    }
}
```

### Pattern 2: Argument Matchers

```java
@Test
void testSaveUser() {
    // Using ArgumentMatchers for flexible matching
    AppUser user = new AppUser();
    user.setEmail("newuser@example.com");
    
    when(userRepository.save(any(AppUser.class))).thenReturn(user);
    
    AppUser result = userService.createUser(user);
    
    assertNotNull(result);
    verify(userRepository).save(argThat(u -> u.getEmail().contains("@")));
}
```

### Pattern 3: Exception Handling

```java
@Test
void testHandleException() {
    Long userId = 999L;
    
    // Stubbing to throw exception
    when(userRepository.findById(userId)).thenThrow(
        new EntityNotFoundException("User not found")
    );
    
    // Assert exception is thrown
    assertThrows(EntityNotFoundException.class, () -> {
        userService.findUser(userId);
    });
}
```

### Pattern 4: Multiple Calls with Different Returns

```java
@Test
void testMultipleCalls() {
    // Mock returns different values for consecutive calls
    when(userRepository.count())
        .thenReturn(5L)
        .thenReturn(6L)
        .thenReturn(7L);
    
    assertEquals(5L, userService.getUserCount());
    assertEquals(6L, userService.getUserCount());
    assertEquals(7L, userService.getUserCount());
}
```

### Pattern 5: Spying on Real Objects

```java
@Test
void testSpying() {
    AppUser realUser = new AppUser();
    
    // Create a spy (partial mock)
    AppUser spyUser = spy(realUser);
    
    // Stub a specific method
    when(spyUser.getEmail()).thenReturn("spy@example.com");
    
    // Call real methods that aren't stubbed
    assertEquals("spy@example.com", spyUser.getEmail());
}
```

## Testing Controllers with Mockito

```java
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void testGetUser() throws Exception {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setEmail("test@example.com");
        
        when(userService.findUser(1L)).thenReturn(user);
        
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@example.com"));
        
        verify(userService).findUser(1L);
    }
}
```

## Testing Services with Mockito

```java
@ExtendWith(MockitoExtension.class)
class MedicinesServiceTest {
    
    @Mock
    private MedicinesRepository medicinesRepository;
    
    @Mock
    private TipoDeMedicamentoRepository typeRepository;
    
    @InjectMocks
    private MedicinesService medicinesService;
    
    @Test
    void testCreateMedicine() {
        Medicamentos medicine = new Medicamentos();
        medicine.setNombre("Aspirin");
        medicine.setPrecio(10.0);
        
        when(medicinesRepository.save(any(Medicamentos.class)))
            .thenReturn(medicine);
        
        Medicamentos result = medicinesService.create(medicine);
        
        assertNotNull(result);
        assertEquals("Aspirin", result.getNombre());
        verify(medicinesRepository).save(medicine);
    }
}
```

## Important Annotations

| Annotation | Purpose |
|-----------|---------|
| `@Mock` | Creates a mock object |
| `@InjectMocks` | Creates object and injects mocks into it |
| `@Spy` | Creates a spy (partial mock) |
| `@Captor` | Captures arguments passed to mocks |
| `@ExtendWith(MockitoExtension.class)` | Enables Mockito annotations in JUnit 5 |

## Verification Methods

```java
// Verify called exactly once
verify(repository, times(1)).save(user);

// Verify never called
verify(repository, never()).delete(user);

// Verify called at least once
verify(repository, atLeastOnce()).findAll();

// Verify called exactly N times
verify(repository, times(3)).save(user);

// Verify the order of calls
InOrder inOrder = inOrder(repository, service);
inOrder.verify(repository).save(user);
inOrder.verify(service).notifyUser(user);
```

## Common Argument Matchers

```java
when(repository.save(any(User.class))).thenReturn(user);
when(repository.findById(anyLong())).thenReturn(Optional.of(user));
when(repository.search(contains("test"))).thenReturn(list);
when(repository.find(eq("exact"), anyInt())).thenReturn(user);
when(repository.list(argThat(l -> l.size() > 0))).thenReturn(list);
```

## Best Practices

1. **One assertion per test method** - Focus on testing one behavior
2. **Use @ExtendWith(MockitoExtension.class)** - Don't use deprecated runners
3. **Clear naming** - Name tests as `test<Method><Condition><Expected>`
4. **Arrange-Act-Assert** - Structure tests with clear sections
5. **Mock only what's needed** - Keep mocks minimal to avoid over-specification
6. **Avoid mocking everything** - Mock external dependencies, not entities
7. **Use argument matchers wisely** - They can make tests harder to understand

## Running Tests

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run specific test method
mvn test -Dtest=UserServiceTest#testFindUser

# Run tests matching pattern
mvn test -Dtest=*ServiceTest

# Run with coverage
mvn clean test jacoco:report
```

## Example Project Structure

```
src/test/java/com/example/clinicalolimsa/
├── controllers/
│   ├── UserControllerTest.java
│   ├── MedicineControllerTest.java
│   └── CitasControllerTest.java
├── services/
│   ├── UserServiceTest.java
│   ├── MedicinesServiceTest.java
│   └── CitasServiceTest.java
└── repositories/
    └── (Optional repository tests)
```

## Tips & Tricks

1. **Reset mocks between tests**
   ```java
   @BeforeEach
   void setUp() {
       reset(userRepository);
   }
   ```

2. **Capture arguments**
   ```java
   @Captor
   ArgumentCaptor<AppUser> userCaptor;
   
   verify(repository).save(userCaptor.capture());
   AppUser captured = userCaptor.getValue();
   ```

3. **Custom answers**
   ```java
   when(repository.findById(any())).thenAnswer(invocation -> 
       Optional.of(new AppUser((Long) invocation.getArgument(0)))
   );
   ```

## Resources

- [Mockito Official Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Baeldung Mockito Tutorial](https://www.baeldung.com/mockito-series)
- [Spring Boot Testing Guide](https://spring.io/guides/gs/testing-web/)

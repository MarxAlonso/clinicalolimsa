# Quick Reference Guide

Fast reference for the most common testing and coverage commands.

## Quick Start (Copy & Paste)

### Run all tests
```bash
mvn clean test
```

### Generate coverage report
```bash
mvn clean test jacoco:report && open target/site/jacoco/index.html
```

### Run specific test
```bash
mvn test -Dtest=UserServiceTest
```

### Verify coverage threshold
```bash
mvn verify
```

## One-Liners

### Run tests with live view (keep updating)
```bash
# Linux/Mac
watch -n 2 'mvn test 2>/dev/null | tail -20'
```

### Generate report and view immediately
```bash
# Mac
mvn clean test jacoco:report && open target/site/jacoco/index.html

# Linux
mvn clean test jacoco:report && xdg-open target/site/jacoco/index.html

# Windows PowerShell
mvn clean test jacoco:report; Start-Process target/site/jacoco/index.html
```

### Show coverage summary
```bash
mvn clean test jacoco:report && cat target/site/jacoco/*.csv | head -20
```

## Common Test Patterns

### Mock a Repository
```java
@Mock
private UserRepository userRepository;

@InjectMocks
private UserService userService;

@Test
void test() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    // test code
}
```

### Mock a Service in Controller
```java
@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    private UserService userService;
    
    @Autowired
    private MockMvc mockMvc;
}
```

### Test an Exception
```java
@Test
void testThrowsException() {
    when(userRepository.findById(99L))
        .thenThrow(new EntityNotFoundException());
    
    assertThrows(EntityNotFoundException.class, 
        () -> userService.findUser(99L));
}
```

### Verify Mock Was Called
```java
// Verify called once
verify(userRepository, times(1)).save(user);

// Verify called at least once
verify(userRepository, atLeastOnce()).findAll();

// Verify never called
verify(userRepository, never()).delete(user);
```

## File Structure Checklist

### Required
```
✓ src/test/java/com/example/clinicalolimsa/
  ✓ controllers/      (Controller tests)
  ✓ services/        (Service tests)
  ✓ repositories/    (Repository tests if needed)
```

### Optional but Recommended
```
○ src/test/java/com/example/clinicalolimsa/
  ○ security/        (Security tests)
  ○ fixtures/        (Test data builders)
  ○ utils/           (Test utilities)
```

## Maven Command Variations

```bash
# Test single method
mvn test -Dtest=UserServiceTest#testFindUser

# Test matching pattern
mvn test -Dtest=*ServiceTest

# Test with specific configuration
mvn test -Dspring.profiles.active=test

# Skip tests
mvn package -DskipTests

# Force rebuild
mvn clean -U test

# With debug output
mvn test -X

# Parallel test execution
mvn test -T 1C  # 1 thread per core
```

## Coverage Metrics Quick Reference

| Metric | Target | Good | Acceptable | Poor |
|--------|--------|------|------------|------|
| **Line** | 80% | >80% | 50-80% | <50% |
| **Branch** | 75% | >75% | 40-75% | <40% |
| **Method** | 85% | >85% | 60-85% | <60% |

## Report Locations

```
After: mvn clean test jacoco:report

Generated files:
  target/site/jacoco/index.html         # Main report
  target/site/jacoco/jacoco.csv         # CSV data
  target/site/jacoco/jacoco.xml         # XML data (for CI)
  target/site/jacoco/com/.../*.html     # Package reports
```

## Aliases (Add to ~/.bashrc or ~/.zshrc)

```bash
# Make shortcuts for common commands
alias mvnt='mvn clean test'
alias mvnc='mvn clean test jacoco:report'
alias mvnv='mvn verify'
alias mvndoc='mvn clean test jacoco:report && open target/site/jacoco/index.html'
```

Then use:
```bash
mvnt          # Run tests
mvnc          # Generate coverage
mvnv          # Verify threshold
mvndoc        # View coverage report
```

## Troubleshooting Quick Fixes

| Problem | Quick Fix |
|---------|-----------|
| Tests not found | Ensure file is named `*Test.java` in `src/test/java/` |
| Mock not injected | Add `@ExtendWith(MockitoExtension.class)` |
| Coverage report missing | Run `mvn clean test jacoco:report` |
| Coverage too low | Run `open target/site/jacoco/index.html` and add tests |
| Build fails on threshold | Add more tests or lower `<minimum>` in pom.xml |

## Test Naming Convention

```
testNameOfMethod_Condition_ExpectedResult

Good examples:
✓ testFindUserById_WithValidId_ReturnsUser
✓ testCreateUser_WithDuplicateEmail_ThrowsException
✓ testDeleteUser_WithNonExistentId_ReturnsNotFound

Avoid:
✗ test1()
✗ testMethod()
✗ testAll()
```

## Assertion Quick Reference

```java
// Equality
assertEquals(expected, actual);
assertNotEquals(unexpected, actual);

// Null checks
assertNull(object);
assertNotNull(object);

// Boolean
assertTrue(condition);
assertFalse(condition);

// Collections
assertTrue(list.contains(item));
assertEquals(3, list.size());

// Exceptions
assertThrows(Exception.class, () -> code());

// Strings
assertTrue(text.contains("substring"));
assertEquals("expected", actual);
```

## Mockito Quick Reference

```java
// Create mock
@Mock
UserRepository repository;

// Create spy (partial mock)
@Spy
@InjectMocks
UserService service;

// Stub a method
when(repository.findById(1L)).thenReturn(Optional.of(user));

// Verify called
verify(repository).save(user);
verify(repository, times(2)).findAll();
verify(repository, never()).delete(user);

// Argument matchers
any(User.class)
anyLong()
anyString()
contains("text")
argThat(u -> u.getEmail().contains("@"))
```

## IDE Integration

### IntelliJ IDEA / JetBrains
```
Run → Edit Configurations → + Maven
Name: Test Coverage
Working directory: $ProjectFileDir$
Command line: clean test jacoco:report

Then: Run → Run Configurations → Test Coverage
```

### VS Code
```json
// .vscode/tasks.json
{
  "label": "Run Tests with Coverage",
  "type": "shell",
  "command": "mvn",
  "args": ["clean", "test", "jacoco:report"]
}
```

### Eclipse
```
Run → Run Configurations → Maven Build
Goals: clean test jacoco:report
Base directory: ${project_loc}
```

## Environment Setup Check

```bash
# Verify all requirements
echo "Java version:"
java -version

echo "Maven version:"
mvn -version

echo "Git version:"
git --version

echo "Verify dependencies:"
mvn dependency:tree | grep -i "mockito\|jacoco"
```

## Performance Tips

```bash
# Run tests in parallel (faster)
mvn -T 1C test

# Skip javadoc generation (faster builds)
mvn -DskipTests clean package

# Use offline mode (if dependencies cached)
mvn -o test

# Skip checksums (CI environments)
mvn -nsu clean test
```

## CI/CD Variables

```bash
# For GitHub Actions
# Use: ${{ github.workspace }}/target/site/jacoco/

# For Jenkins
# Use: ${WORKSPACE}/target/site/jacoco/

# For GitLab CI
# Use: $CI_PROJECT_DIR/target/site/jacoco/
```

## See Also

- [MOCKITO_GUIDE.md](MOCKITO_GUIDE.md) - Detailed Mockito examples
- [JACOCO_GUIDE.md](JACOCO_GUIDE.md) - Coverage configuration details
- [TESTING_WORKFLOW.md](TESTING_WORKFLOW.md) - Complete development workflow
- [README.md](README.md) - Overview and resources

# Testing & Code Coverage Documentation

This documentation provides comprehensive guides on using Mockito for unit testing and Jacoco for code coverage analysis in the Clínica Lolimsa application.

## Quick Start

### Prerequisites
- Maven 3.6+
- Java 24+
- Git (optional)

### Run All Tests
```bash
mvn clean test
```

### Generate Code Coverage Report
```bash
mvn clean test jacoco:report
```

### View Coverage Report
After generating the report, open it in a browser:
```bash
# On Linux/Mac
open target/site/jacoco/index.html

# On Windows
start target/site/jacoco/index.html
```

## Documentation Files

1. **[MOCKITO_GUIDE.md](MOCKITO_GUIDE.md)** - Comprehensive guide to Mockito framework
   - What is Mockito?
   - Basic mocking patterns
   - Example implementations
   - Best practices

2. **[JACOCO_GUIDE.md](JACOCO_GUIDE.md)** - Code coverage with Jacoco
   - What is Jacoco?
   - Configuration details
   - Running coverage analysis
   - Interpreting results

3. **[TESTING_WORKFLOW.md](TESTING_WORKFLOW.md)** - Complete testing workflow
   - Development cycle best practices
   - Running tests locally
   - CI/CD integration suggestions
   - Common issues and solutions

## Project Structure

```
clinicalolimsa/
├── src/
│   ├── main/
│   │   └── java/com/example/clinicalolimsa/
│   │       ├── controllers/      # REST controllers
│   │       ├── models/           # Entity models
│   │       ├── repositories/     # Data access layer
│   │       ├── security/         # Security configuration
│   │       └── jwt/             # JWT utilities
│   └── test/
│       └── java/com/example/clinicalolimsa/
│           └── (Test classes go here)
├── pom.xml                      # Maven configuration with Mockito & Jacoco
└── docs/                         # This documentation
```

## Key Dependencies

- **Spring Boot 3.4.5** - Application framework
- **Mockito** - Mocking framework for unit tests
- **Jacoco** - Code coverage analysis tool
- **Spring Security Test** - Security testing utilities
- **JUnit 5** - Test framework (included with Spring Boot Test)

## Common Commands

| Command | Purpose |
|---------|---------|
| `mvn clean test` | Run all tests |
| `mvn test -Dtest=TestClassName` | Run specific test class |
| `mvn test -Dtest=TestClassName#testMethodName` | Run specific test method |
| `mvn clean test jacoco:report` | Run tests and generate coverage report |
| `mvn jacoco:check` | Verify coverage meets minimum thresholds |
| `mvn verify` | Run all tests and checks |

## Coverage Threshold

The Jacoco configuration requires a minimum of **50% line coverage** for packages. This ensures basic quality standards while allowing gradual improvement.

## Next Steps

1. Start with [MOCKITO_GUIDE.md](MOCKITO_GUIDE.md) to understand mocking patterns
2. Follow [TESTING_WORKFLOW.md](TESTING_WORKFLOW.md) for the development process
3. Use [JACOCO_GUIDE.md](JACOCO_GUIDE.md) for coverage analysis and reports

## Troubleshooting

### Tests won't run
- Verify Java version: `java -version` (should be 24)
- Verify Maven: `mvn -version`
- Clean and rebuild: `mvn clean install`

### Coverage report not generated
- Run: `mvn clean test jacoco:report`
- Check `target/site/jacoco/` folder exists

### Coverage threshold failures
- Review failing classes in `target/site/jacoco/`
- Add unit tests for uncovered code
- See [JACOCO_GUIDE.md](JACOCO_GUIDE.md) for details

## Additional Resources

- [Mockito Official Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Jacoco Official Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [Spring Testing Guide](https://spring.io/guides/gs/testing-web/)
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)

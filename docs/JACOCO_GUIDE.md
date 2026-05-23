# JaCoCo Code Coverage Guide

## What is JaCoCo?

JaCoCo (Java Code Coverage) is an open-source code coverage library that provides insights into which parts of your code are being tested. It measures:

- **Line Coverage**: Which lines of code are executed during tests
- **Branch Coverage**: Which decision branches are tested
- **Method Coverage**: Which methods are called during tests
- **Class Coverage**: Which classes have test coverage

### Benefits
- **Quality Metrics**: Quantify test coverage
- **Identify Gaps**: Find untested code
- **Track Progress**: Monitor coverage improvements over time
- **Enforce Standards**: Set minimum coverage requirements

## Configuration

JaCoCo is already configured in the project's `pom.xml`:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>jacoco-check</id>
            <phase>test</phase>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>PACKAGE</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.50</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Current Settings

- **Minimum Coverage**: 50% line coverage per package
- **Report Generation**: Automatic on test execution
- **Report Location**: `target/site/jacoco/`

## Running Coverage Analysis

### Generate Coverage Report Only

```bash
mvn clean test jacoco:report
```

This command:
1. Cleans previous builds (`clean`)
2. Compiles code and runs tests (`test`)
3. Generates JaCoCo coverage report (`jacoco:report`)

### Check Coverage Thresholds

```bash
mvn clean test jacoco:check
```

This verifies that coverage meets the minimum requirements (50%). Build fails if threshold is not met.

### Full Verification

```bash
mvn clean verify
```

Runs all tests and verifies coverage requirements.

## Viewing the Report

### HTML Report

After running `mvn clean test jacoco:report`, open the HTML report:

```bash
# Linux/Mac
open target/site/jacoco/index.html

# Windows
start target/site/jacoco/index.html

# WSL
wslview target/site/jacoco/index.html
```

### Report Structure

```
target/site/jacoco/
├── index.html              # Coverage overview
├── jacoco-resources/       # CSS and JavaScript
├── com/
│   └── example/
│       └── clinicalolimsa/
│           ├── index.html
│           └── [Package and class reports]
└── jacoco.csv              # Machine-readable coverage data
```

## Understanding the Report

### Coverage Metrics

Each report shows:
- **Instruction Coverage**: Percentage of bytecode instructions executed
- **Line Coverage**: Percentage of source lines executed
- **Branch Coverage**: Percentage of conditional branches tested
- **Method Coverage**: Percentage of methods called
- **Class Coverage**: Percentage of classes with test coverage

### Color Coding

- 🟢 **Green**: High coverage (>80%)
- 🟡 **Yellow**: Medium coverage (50-80%)
- 🔴 **Red**: Low coverage (<50%)

### Example Interpretation

```
Package: com.example.clinicalolimsa.controllers
├── LineRate: 75%        ← Good coverage
├── BranchRate: 65%      ← Acceptable but could improve
└── ClassCoverage: 100%  ← All classes tested

class UserController
├── Methods: 8 of 10 covered (80%)
└── Lines: 145 of 180 covered (80%)
```

## Improving Coverage

### Step 1: Identify Gaps

Review the HTML report to find uncovered code:
1. Open `target/site/jacoco/index.html`
2. Click on classes with low coverage
3. Red lines indicate uncovered code

### Step 2: Write Tests

Create unit tests for uncovered code. Example:

```java
// Before: No test for error case
public AppUser findUser(Long id) {
    return userRepository.findById(id)
        .orElse(null);
}

// After: Add test for error case
@Test
void testFindUserNotFound() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());
    AppUser result = userService.findUser(99L);
    assertNull(result);
    verify(userRepository).findById(99L);
}
```

### Step 3: Verify Improvement

```bash
mvn clean test jacoco:report
# Review target/site/jacoco/index.html
```

## Coverage Standards by Component

### Controllers
- **Target**: 70-80% coverage
- **Focus**: Test happy paths and error cases
- **Example**:
  ```java
  @WebMvcTest(MedicinesController.class)
  class MedicinesControllerTest {
      // Test endpoints, validation, error handling
  }
  ```

### Services
- **Target**: 80-90% coverage
- **Focus**: All business logic paths
- **Example**:
  ```java
  @ExtendWith(MockitoExtension.class)
  class MedicinesServiceTest {
      // Test all methods with various inputs
  }
  ```

### Repositories
- **Target**: 50-70% coverage
- **Focus**: Custom query methods
- **Note**: Spring Data Repository auto-generated methods may not need explicit tests

### Models/Entities
- **Target**: 30-50% coverage
- **Note**: Getters/setters often skipped with Lombok

## Advanced Configuration

### Excluding Classes from Coverage

Modify `pom.xml` to exclude certain packages:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <configuration>
        <excludes>
            <exclude>com/example/clinicalolimsa/models/**</exclude>
            <exclude>**/*Dto.class</exclude>
            <exclude>**/*Application.class</exclude>
        </excludes>
    </configuration>
</plugin>
```

### Custom Coverage Rules

Require different thresholds for different components:

```xml
<rules>
    <!-- Controllers: 70% line coverage -->
    <rule>
        <element>PACKAGE</element>
        <includes>
            <include>com.example.clinicalolimsa.controllers</include>
        </includes>
        <limits>
            <limit>
                <counter>LINE</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.70</minimum>
            </limit>
        </limits>
    </rule>
    
    <!-- Services: 80% line coverage -->
    <rule>
        <element>PACKAGE</element>
        <includes>
            <include>com.example.clinicalolimsa.services</include>
        </includes>
        <limits>
            <limit>
                <counter>LINE</counter>
                <value>COVEREDRATIO</value>
                <minimum>0.80</minimum>
            </limit>
        </limits>
    </rule>
</rules>
```

## Common Issues & Solutions

### Issue 1: Report Not Generated

**Problem**: `target/site/jacoco/` directory doesn't exist

**Solution**:
```bash
# Ensure tests run and generate report
mvn clean test jacoco:report

# Check if tests passed
mvn clean test -X  # verbose output
```

### Issue 2: Coverage Lower Than Expected

**Problem**: Coverage report shows lower coverage despite passing tests

**Solutions**:
1. Verify tests are actually running: `mvn test -X`
2. Check test file location: Must be in `src/test/java/`
3. Ensure test files are named `*Test.java` or use `<includes>`
4. Rebuild: `mvn clean test`

### Issue 3: Build Fails Due to Coverage Threshold

**Problem**: `BUILD FAILURE: Code coverage is below minimum threshold`

**Solutions**:
1. Write more tests for uncovered code
2. Lower threshold temporarily (not recommended):
   ```xml
   <minimum>0.40</minimum>  <!-- Lower from 0.50 -->
   ```
3. Exclude specific packages
4. Review coverage report for gaps

### Issue 4: Can't View HTML Report

**Problem**: Browser won't open the HTML file

**Solutions**:
```bash
# Ensure file exists
ls -la target/site/jacoco/index.html

# Try different browsers or tools
# Linux
firefox target/site/jacoco/index.html
chromium target/site/jacoco/index.html

# Mac
open -a "Google Chrome" target/site/jacoco/index.html

# Windows
powershell -Command "Start-Process target/site/jacoco/index.html"
```

## CI/CD Integration

### GitHub Actions Example

```yaml
name: Test Coverage

on: [push, pull_request]

jobs:
  coverage:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      
      - name: Set up Java
        uses: actions/setup-java@v2
        with:
          java-version: '24'
          distribution: 'temurin'
      
      - name: Run tests with coverage
        run: mvn clean test jacoco:report
      
      - name: Upload coverage to Codecov
        uses: codecov/codecov-action@v2
        with:
          files: ./target/site/jacoco/jacoco.xml
```

### GitLab CI Example

```yaml
test:
  stage: test
  script:
    - mvn clean test jacoco:report
  artifacts:
    paths:
      - target/site/jacoco/
    reports:
      coverage_report:
        coverage_format: cobertura
        path: target/site/jacoco/jacoco.xml
```

## Best Practices

1. **Set Realistic Targets**: Start with 50%, gradually increase to 80%
2. **Focus on Critical Code**: Prioritize service and controller tests
3. **Use Coverage Metrics Wisely**: 100% coverage ≠ quality tests
4. **Combine with Code Review**: Coverage is one metric, not the only one
5. **Monitor Trends**: Track coverage over time
6. **Exclude Generated Code**: DTOs, entities with only getters/setters
7. **Test Business Logic**: Don't aim for coverage in trivial code

## Example Workflow

```bash
# 1. Write code
# Edit src/main/java/...

# 2. Run tests locally
mvn clean test

# 3. Check coverage
mvn test jacoco:report

# 4. Review gaps
open target/site/jacoco/index.html

# 5. Write missing tests
# Edit src/test/java/...

# 6. Verify improvement
mvn clean test jacoco:report

# 7. Commit when satisfied
git add .
git commit -m "Add tests for UserService"
```

## Resources

- [JaCoCo Official Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [Maven JaCoCo Plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
- [Code Coverage Best Practices](https://www.baeldung.com/jacoco)
- [Branch vs Line Coverage](https://stackoverflow.com/questions/195008/code-coverage-metrics)

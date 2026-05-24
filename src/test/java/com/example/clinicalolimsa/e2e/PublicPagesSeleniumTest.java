package com.example.clinicalolimsa.e2e;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Selenium Tests - Public Pages")
class PublicPagesSeleniumTest {

    private static final boolean HEADLESS = Boolean.parseBoolean(
            System.getProperty("selenium.headless", "false")
    );

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        if (HEADLESS) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,900");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Home page should load")
    void homePageShouldLoad() {
        driver.get(baseUrl + "/");

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(webDriver -> webDriver.getTitle().contains("Clinica Lolimsa"));

        assertTrue(driver.getPageSource().contains("Bienvenidos a Clínica Lolimsa"));
        assertTrue(driver.findElement(By.linkText("Iniciar Sesión")).isDisplayed());
    }

    @Test
    @DisplayName("Login page should render the form")
    void loginPageShouldRenderForm() {
        driver.get(baseUrl + "/login");

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(webDriver -> webDriver.getTitle().contains("Login - Clínica Lolimsa"));

        assertEquals("INICIAR SESIÓN", driver.findElement(By.tagName("h2")).getText());
        assertTrue(driver.findElement(By.name("email")).isDisplayed());
        assertTrue(driver.findElement(By.name("password")).isDisplayed());
    }
}
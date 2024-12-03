package org.example;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Paths;

public class SimplexFormTest
{
    private static Playwright playwright;
    private static Browser browser;
    private Page page;

    @BeforeAll
    public static void setUpClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    public static void tearDownClass() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    public void setUp() {
        page = browser.newPage();
    }

    @AfterEach
    public void tearDown() {
        page.close();
    }

    @Test
    public void testSelectVariablesAndConstraints() {
        page.navigate("https://www.pmcalculators.com/simplex-method-calculator/");

        // Select the number of variables and constraints
        page.fill("#cantidad-variables", "2");
        page.fill("#cantidad-restricciones", "3");

        // Take a screenshot after selecting variables and constraints
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("after_selecting_vars_constraints.png")));

        // Click the generate model button
        page.click("#boton-dos-fases");

        // Wait for the form to be generated
        page.waitForSelector("#formulario-problema");

        // Fill in the objective
        page.selectOption("#seleccionar-objetivo", "Maximizar");

        // Fill in the objective function
        page.fill("#x1", "3");
        page.fill("#x2", "5");

        // Fill in constraints
        page.fill("#x1-1", "1");
        page.fill("#x1-2", "0");
        page.selectOption("#signo1", "≤");
        page.fill("#y1", "4");

        page.fill("#x2-1", "0");
        page.fill("#x2-2", "2");
        page.selectOption("#signo2", "≤");
        page.fill("#y2", "12");

        page.fill("#x3-1", "3");
        page.fill("#x3-2", "2");
        page.selectOption("#signo3", "≤");
        page.fill("#y3", "18");

        // Take a screenshot before submitting the form
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("before_submit.png")));

        // Submit the form
        page.click("#resolver-Simplex");

        // Wait for the result to be visible
        page.waitForSelector(".alert-success");

        // Take a screenshot after submitting the form
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("after_submit.png")));

        // Verify the result
        String result = page.textContent(".alert-success");
        assertTrue(result.contains("X1= 2"), "X1 should be 2");
        assertTrue(result.contains("X2= 6"), "X2 should be 6");
        assertTrue(result.contains("S1= 2"), "S1 should be 2");
        assertTrue(result.contains("S2= 0"), "S2 should be 0");
        assertTrue(result.contains("S3= 0"), "S3 should be 0");
    }
}
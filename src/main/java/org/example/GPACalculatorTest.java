package org.example;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Paths;

public class GPACalculatorTest
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
    public void testGpaCalculator() {
        page.navigate("https://www.calculator.net/gpa-calculator.html");

        // Fill in the form with sample data
        page.fill("input[name='da1']", "Math");
        page.fill("input[name='ca1']", "3");
        page.selectOption("select[name='la1']", "A");

        page.fill("input[name='da2']", "English");
        page.fill("input[name='ca2']", "3");
        page.selectOption("select[name='la2']", "B+");

        page.fill("input[name='da3']", "History");
        page.fill("input[name='ca3']", "2");
        page.selectOption("select[name='la3']", "A-");

        // Take a screenshot before clicking the calculate button
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("before_calculate_gpa.png")));

        // Click the calculate button
        page.click("input[name='x']");

        // Take a screenshot after the result is visible
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("after_calculate_gpa.png")));

        // Verify the calculated GPA
        String gpaResult = page.textContent("b:has-text('GPA')");
        assertTrue(gpaResult.contains("3.663"), "GPA should be 3.663");
    }

    @Test
    public void testGpaPlanning() {
        page.navigate("https://www.calculator.net/gpa-calculator.html");

        // Fill in the form with sample data
        page.fill("input[name='grcCurrentGPA']", "2.8");
        page.fill("input[name='grcTargetGPA']", "3");
        page.fill("input[name='grcCurrentCredit']", "25");
        page.fill("input[name='grcAdditionalCredit']", "15");

        // Take a screenshot before clicking the calculate button
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("before_calculate_gpa_planning.png")));

        // Click the calculate button for GPA Planning
        page.click("form[name='gparaiseform'] input[name='x']");

        // Take a screenshot after the result is visible
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("after_calculate_gpa_planning.png")));

        // Verify the calculated GPA
        String gpaResult = page.textContent("p.bigtext");
        assertTrue(gpaResult.contains("3.333"), "GPA for the next 15 credits should be 3.333 or higher");
    }
}
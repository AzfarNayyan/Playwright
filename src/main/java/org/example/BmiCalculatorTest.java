package org.example;

import com.microsoft.playwright.*;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;

public class BmiCalculatorTest
{
    private static Playwright playwright;
    private static Browser browser;
    private Page page;

    @BeforeAll
    public static void setUpClass()
    {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    public static void tearDownClass()
    {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    public void setUp()
    {
        page = browser.newPage();
    }

    @AfterEach
    public void tearDown() {
        page.close();
    }

    @Test
    public void testBmiCalculator()
    {
        page.navigate("https://www.heartonline.org.au/resources/calculators/bmi-calculator");

        // Select gender
        page.click("img#male");

        // Select Imperial system
        page.selectOption("select#changeUnit", "1");

        // Fill in height and weight directly into the input boxes
        page.fill("input[data-ng-model='feet']", "5");
        page.fill("input[data-ng-model='inches']", "11");
        page.fill("input[data-ng-model='sliderWeight2.weightValue']", "165");

        // Take a screenshot before clicking the calculate button
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("before_calculate.png")));

        // Click the calculate button
        page.click("a.button");

        // Wait for the result to be visible
        page.waitForSelector("div.bmi-result");

        // Take a screenshot after the result is visible
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("after_calculate.png")));

        // Verify the calculated BMI
        String bmiResult = page.textContent("td.ng-binding");
        assertTrue(bmiResult.contains("23.0"), "BMI should be 23.0");
    }
}
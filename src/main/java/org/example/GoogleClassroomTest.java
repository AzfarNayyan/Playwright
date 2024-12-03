package org.example;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Paths;

public class GoogleClassroomTest
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
    public void testGoogleClassroomLogin()
    {
        page.navigate("https://accounts.google.com/v3/signin/identifier?continue=https%3A%2F%2Fclassroom.google.com&ifkv=AcMMx-ejtSuFPey1ge3LZ8WkftR8og7WV5vnrPy4xVhHhbTO3axRn6qShGJZPbeaSDVzuFpdZkiFyw&passive=true&flowName=GlifWebSignIn&flowEntry=ServiceLogin&dsh=S-334511631%3A1732900733810042&ddm=1");

        // Take a screenshot before filling the email field
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("before_email.png")));

        // Fill in the email field
        page.fill("input[type='email']", "l227886@lhr.nu.edu.pk");
        page.click("button:has-text('Next')");

        // Wait for the password field to be visible
        page.waitForSelector("input[type='password']");

        // Take a screenshot before filling the password field
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("before_password.png")));

        page.fill("input[type='password']", "Azfarloginsintofastkiemail786");

        // Click the sign in button and wait for navigation
        page.waitForNavigation(() -> {
            page.click("button:has-text('Next')");
        });

        // Take a screenshot after login
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("after_login.png")));

        // Verify successful login by checking the URL or a specific element
        assertTrue(page.url().contains("classroom.google.com"));
    }
}
package org.example;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlaywrightTest
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

        // Fill in the email field
        page.fill("input[type='email']", "email");
        page.click("button:has-text('Next')");

        // Wait for the password field to be visible
        page.waitForSelector("input[type='password']");
        page.fill("input[type='password']", "password");

        // Click the sign in button and wait for navigation
        page.waitForNavigation(() -> {
            page.click("button:has-text('Next')");
        });

        // Verify successful login by checking the URL or a specific element
        assertTrue(page.url().contains("classroom.google.com"));
        assertTrue(page.isVisible("div[aria-label='Google Account']"));
    }
}
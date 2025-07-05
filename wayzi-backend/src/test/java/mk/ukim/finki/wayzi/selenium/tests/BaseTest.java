package mk.ukim.finki.wayzi.selenium.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import mk.ukim.finki.wayzi.selenium.pages.LoginPage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    protected WebDriver driver;

    /**
     * Sets up the WebDriver before all tests in the class.
     * Initializes the ChromeDriver using WebDriverManager.
     */
    @BeforeAll
    void setupDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("start-maximized");

        driver = new ChromeDriver(options);
    }

    /**
     * Cleans up the WebDriver after all tests in the class.
     * Ensures the browser is closed properly.
     */
    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void loginAs(String email, String password) {
        assertTrue(LoginPage.performLogin(driver, email, password));
    }
}

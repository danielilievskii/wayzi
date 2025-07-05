package mk.ukim.finki.wayzi.selenium.tests;

import mk.ukim.finki.wayzi.selenium.pages.HomePage;
import mk.ukim.finki.wayzi.selenium.pages.LoginPage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginPageTest extends BaseTest {

    /**
     * Tests the successful login functionality.
     * Verifies that the user is redirected to the HomePage after a successful login.
     */
    @Test
    void shouldLoginSuccessfully() {
        String username = "daniel@gmail.com";
        String password = "12345#*lozinka";

//        HomePage homePage = LoginPage.login(driver, username, password);
//        assertNotNull(homePage);

        assertTrue(LoginPage.performLogin(driver, username, password));
        assertTrue(HomePage.isPageFullyLoaded(driver));
    }

    /**
     * Tests the failed login functionality.
     * Verifies that the user remains on the LoginPage after a failed login attempt and an error is visible.
     */
    @Test
    void shouldShowError_whenUserNotFound() {
        String username = "admin@admin.com";
        String password = "admin";

        assertFalse(LoginPage.performLogin(driver, username, password));
        assertTrue(LoginPage.isPageFullyLoaded(driver));
        assertTrue(LoginPage.isLoginErrorVisible(driver));
    }

    /**
     * Tests the failed login functionality.
     * Verifies that the user remains on the LoginPage after a failed login attempt and an error is visible.
     */
    @Test
    void shouldShowError_whenIncorrectPassword() {
        String username = "daniel@gmail.com";
        String password = "12345";

        assertFalse(LoginPage.performLogin(driver, username, password));
        assertTrue(LoginPage.isPageFullyLoaded(driver));
        assertTrue(LoginPage.isLoginErrorVisible(driver));
    }
}


package mk.ukim.finki.wayzi.selenium.pages;

import lombok.Getter;
import mk.ukim.finki.wayzi.selenium.base.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Getter
public class LoginPage extends AbstractPage {

    private static final String USERNAME_FIELD_SELECTOR = "input[name='username']";
    private static final String PASSWORD_FIELD_SELECTOR = "input[name='password']";
    private static final String SUBMIT_BUTTON_SELECTOR = "button[type='submit']";
    private static final String ERROR_MESSAGE_SELECTOR = "div.alert-danger";

    @FindBy(css = USERNAME_FIELD_SELECTOR)
    private WebElement usernameField;

    @FindBy(css = PASSWORD_FIELD_SELECTOR)
    private WebElement passwordField;

    @FindBy(css = SUBMIT_BUTTON_SELECTOR)
    private WebElement submitButton;

    @FindBy(css = SUBMIT_BUTTON_SELECTOR)
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public static HomePage login(WebDriver driver, String username, String password) {
        get(driver, "/login");

        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(LoginPage::isPageFullyLoaded);

        loginPage.usernameField.sendKeys(username);
        loginPage.passwordField.sendKeys(password);
        loginPage.submitButton.click();

        HomePage homePage = PageFactory.initElements(driver, HomePage.class);
        try {
            wait.until(d -> HomePage.isPageFullyLoaded(d) || isLoginErrorVisible(d));
            return homePage;
        } catch (TimeoutException e) {
            return null;
        }
    }

    public static boolean performLogin(WebDriver driver, String username, String password) {
        get(driver, "/login");

        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(LoginPage::isPageFullyLoaded);

        loginPage.usernameField.sendKeys(username);
        loginPage.passwordField.sendKeys(password);
        loginPage.submitButton.click();

        try {
            wait.until(d -> HomePage.isPageFullyLoaded(d) || isLoginErrorVisible(d));
            return HomePage.isPageFullyLoaded(driver);
        } catch (TimeoutException e) {
            return false;
        }
    }

    public static boolean isLoginErrorVisible(WebDriver driver) {
        return !driver.findElements(By.cssSelector(ERROR_MESSAGE_SELECTOR)).isEmpty();
    }

    public static boolean isPageFullyLoaded(WebDriver driver) {
        return areElementsPresentAndVisible(
                driver,
                USERNAME_FIELD_SELECTOR,
                PASSWORD_FIELD_SELECTOR,
                SUBMIT_BUTTON_SELECTOR
        );
    }

}

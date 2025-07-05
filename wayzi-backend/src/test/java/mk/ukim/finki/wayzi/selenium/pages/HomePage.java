package mk.ukim.finki.wayzi.selenium.pages;

import lombok.Getter;
import mk.ukim.finki.wayzi.selenium.base.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Getter
public class HomePage extends AbstractPage {

    private static final String WELCOME_SECTION_SELECTOR = ".landing-page";
    private static final String PUBLISH_RIDE_BUTTON_SELECTOR = "#publishRideButton";

    @FindBy(css = PUBLISH_RIDE_BUTTON_SELECTOR)
    private WebElement publishRideButton;

    @FindBy(css = WELCOME_SECTION_SELECTOR)
    private WebElement welcomeSection;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public static HomePage to(WebDriver driver) {
        get(driver, "/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(HomePage::isPageFullyLoaded);

        return PageFactory.initElements(driver, HomePage.class);
    }


    public static void clickPublishRideButton(WebDriver driver) {
        HomePage homePage = PageFactory.initElements(driver, HomePage.class);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(PUBLISH_RIDE_BUTTON_SELECTOR)));

        homePage.publishRideButton.click();
    }

    public static boolean isPageFullyLoaded(WebDriver driver) {
        return areElementsPresentAndVisible(
                driver,
                WELCOME_SECTION_SELECTOR
        );
    }

}


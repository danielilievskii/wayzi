package mk.ukim.finki.wayzi.selenium.pages;

import lombok.Getter;
import mk.ukim.finki.wayzi.selenium.base.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Getter
public class RidesPage extends AbstractPage {

    private static final String CARD_SELECTOR = ".ride-card";

    @FindBy(css = CARD_SELECTOR)
    private List<WebElement> cards;

    public RidesPage(WebDriver driver) {
        super(driver);
    }

    public static RidesPage to(WebDriver driver) {
        get(driver, "/rides");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(RidesPage::isPageFullyLoaded);

        return PageFactory.initElements(driver, RidesPage.class);
    }

    public static boolean isPageFullyLoaded(WebDriver driver) {
        return AbstractPage.areElementsPresentAndVisible(
                driver,
                CARD_SELECTOR
        );
    }

}

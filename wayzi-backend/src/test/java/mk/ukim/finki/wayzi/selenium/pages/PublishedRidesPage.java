package mk.ukim.finki.wayzi.selenium.pages;

import lombok.Getter;
import mk.ukim.finki.wayzi.selenium.base.AbstractPage;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideDto;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@Getter
public class PublishedRidesPage extends AbstractPage {

    private static final String CARD_SELECTOR = ".ride-card";
    private static final String EMPTY_PAGE_SELECTOR = "#empty-page";
    private static final String TOTAL_ITEMS_SELECTOR = "#totalItems";

    @FindBy(css = CARD_SELECTOR)
    private List<WebElement> cards;

    @FindBy(css = EMPTY_PAGE_SELECTOR)
    private WebElement pageWrapper;

    public PublishedRidesPage(WebDriver driver) {
        super(driver);
    }

    public static PublishedRidesPage to(WebDriver driver) {
        get(driver, "/rides/published");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(PublishedRidesPage::isPageFullyLoaded);

        return PageFactory.initElements(driver, PublishedRidesPage.class);
    }


    public static boolean isPageFullyLoaded(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(TOTAL_ITEMS_SELECTOR)),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(EMPTY_PAGE_SELECTOR))
            ));
            return true;
        } catch (Exception e) {
            return false; // Neither showed up within timeout
        }
    }

    public int getTotalItems() {

        try {
            WebElement totalItems = driver.findElement(By.cssSelector("#totalItems"));
            return Integer.parseInt(totalItems.getText());
        } catch (NoSuchElementException ex) {
            return 0;
        }
    }

    public void assertItems(int expectedItemsNumber) {
        assertEquals("Number of items", expectedItemsNumber, getCards().size());
    }

}

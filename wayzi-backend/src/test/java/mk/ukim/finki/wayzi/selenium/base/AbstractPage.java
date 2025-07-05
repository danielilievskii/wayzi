package mk.ukim.finki.wayzi.selenium.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AbstractPage {

    protected WebDriver driver;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public static void get(WebDriver driver, String relativeUrl) {
        String url = System.getProperty("geb.build.baseUrl", "http://localhost:5173") + relativeUrl;
        driver.get(url);
    }

    protected static boolean areElementsPresentAndVisible(WebDriver driver, String... selectors) {
        for (String selector : selectors) {
            List<WebElement> elements = driver.findElements(By.cssSelector(selector));
            if (elements.isEmpty())
                return false;
        }
        return true;
    }

}

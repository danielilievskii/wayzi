package mk.ukim.finki.wayzi.selenium.pages;

import lombok.Getter;
import mk.ukim.finki.wayzi.selenium.base.AbstractPage;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideDto;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideStopDto;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class PublishRideFormPage extends AbstractPage {

    private static final String SUBMIT_BUTTON_SELECTOR = "button[type='submit']";
    private static final String ADD_STOP_BUTTON_SELECTOR = "#addStopButton";
    private static final String FORM_SELECTOR = "#form";
    private static final String ERROR_MESSAGE_SELECTOR = ".alert-danger";

    private static final String DEPARTURE_NAME_FIELD_SELECTOR = "input[name='departureLocationName']";
    private static final String DEPARTURE_ADDRESS_FIELD_SELECTOR = "input[name='departureAddress']";
    private static final String DEPARTURE_TIME_FIELD_SELECTOR = "input[name='departureTime']";
    private static final String ARRIVAL_NAME_FIELD_SELECTOR = "input[name='arrivalLocationName']";
    private static final String ARRIVAL_ADDRESS_FIELD_SELECTOR = "input[name='arrivalAddress']";
    private static final String ARRIVAL_TIME_FIELD_SELECTOR = "input[name='arrivalTime']";
    private static final String VEHICLE_FIELD_SELECTOR = "select[name='vehicleId']";
    private static final String AVAILABLE_SEATS_FIELD_SELECTOR = "input[name='availableSeats']";
    private static final String PRICE_PER_SEAT_FIELD_SELECTOR = "input[name='pricePerSeat']";

    @FindBy(css = SUBMIT_BUTTON_SELECTOR)
    private WebElement submitButton;

    @FindBy(css = ADD_STOP_BUTTON_SELECTOR)
    private WebElement addStopButton;

    @FindBy(css = FORM_SELECTOR)
    private WebElement form;

    @FindBy(css = DEPARTURE_NAME_FIELD_SELECTOR)
    private WebElement departureNameField;

    @FindBy(css = DEPARTURE_ADDRESS_FIELD_SELECTOR)
    private WebElement departureAddressField;

    @FindBy(css = DEPARTURE_TIME_FIELD_SELECTOR)
    private WebElement departureTimeField;

    @FindBy(css = ARRIVAL_NAME_FIELD_SELECTOR)
    private WebElement arrivalNameField;

    @FindBy(css = ARRIVAL_ADDRESS_FIELD_SELECTOR)
    private WebElement arrivalAddressField;

    @FindBy(css = ARRIVAL_TIME_FIELD_SELECTOR)
    private WebElement arrivalTimeField;

    @FindBy(css = VEHICLE_FIELD_SELECTOR)
    private WebElement vehicleField;

    @FindBy(css = AVAILABLE_SEATS_FIELD_SELECTOR)
    private WebElement availableSeatsField;

    @FindBy(css = PRICE_PER_SEAT_FIELD_SELECTOR)
    private WebElement pricePerSeatField;

    public PublishRideFormPage(WebDriver driver) {
        super(driver);
    }

    public static PublishRideFormPage to(WebDriver driver) {
        get(driver, "/rides/publish");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(PublishRideFormPage::isPageFullyLoaded);

        return PageFactory.initElements(driver, PublishRideFormPage.class);
    }

    public static void publishRide (WebDriver driver, CreateRideDto dto) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mma");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        PublishRideFormPage publishRide = PageFactory.initElements(driver, PublishRideFormPage.class);

        //Departure name
        publishRide.departureNameField.sendKeys("Sko");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".list-group-item")));
        List<WebElement> suggestions = driver.findElements(By.cssSelector(".list-group-item"));
        suggestions.get(0).click();

        //Departure address
        publishRide.departureAddressField.sendKeys(dto.departureAddress());

        //Departure time
        publishRide.departureTimeField.sendKeys(dto.departureTime().format(dateFormatter));
        publishRide.departureTimeField.sendKeys(Keys.TAB);
        publishRide.departureTimeField.sendKeys(dto.departureTime().format(timeFormatter));

        //Arrival name
        publishRide.arrivalNameField.sendKeys("Ohr");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".list-group-item")));
        suggestions = driver.findElements(By.cssSelector(".list-group-item"));
        suggestions.get(0).click();

        //Arrival address
        publishRide.arrivalAddressField.sendKeys(dto.arrivalAddress());

        //Arrival time
        publishRide.arrivalTimeField.sendKeys(dto.arrivalTime().format(dateFormatter));
        publishRide.arrivalTimeField.sendKeys(Keys.TAB);
        publishRide.arrivalTimeField.sendKeys(dto.arrivalTime().format(timeFormatter));

//        Select select = new Select(publishRide.vehicleField);
//        select.getFirstSelectedOption();

        publishRide.availableSeatsField.sendKeys(dto.availableSeats().toString());
        publishRide.pricePerSeatField.sendKeys(dto.pricePerSeat().toString());

        if(!dto.rideStops().isEmpty()) {
            List<WebElement> rows;
            WebElement stopNameField;
            WebElement stopAddress;
            WebElement stopTime;

            for(int i=0; i<dto.rideStops().size(); i++) {
                publishRide.addStopButton.click();

                CreateRideStopDto stopDto = dto.rideStops().get(i);

                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#stopContainer > .row")));
                rows = driver.findElements(By.cssSelector("#stopContainer > .row"));

                //Stop location
                stopNameField = rows.get(i).findElement(By.cssSelector("#stopContainer input.form-control"));
                stopNameField.sendKeys("Val");

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".list-group-item")));
                suggestions = driver.findElements(By.cssSelector(".list-group-item"));
                suggestions.get(0).click();


                //Stop address
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(String.format("input[name='rideStops.%d.stopAddress']", i))
                ));
                stopAddress = rows.get(i).findElement(By.cssSelector(String.format("input[name='rideStops.%d.stopAddress']", i)));
                stopAddress.sendKeys(stopDto.stopAddress());


                //Stop time
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(String.format("input[name='rideStops.%d.stopAddress']", i))
                ));
                stopTime = rows.get(i).findElement(By.cssSelector(String.format("input[name='rideStops.%d.stopTime']", i)));
                stopTime.sendKeys(stopDto.stopTime().format(dateFormatter));
                stopTime.sendKeys(Keys.TAB);
                stopTime.sendKeys(stopDto.stopTime().format(timeFormatter));

            }
        }

        publishRide.submitButton.click();
    }

    public static void submitRide(WebDriver driver) {
        WebElement button = driver.findElement(By.cssSelector(SUBMIT_BUTTON_SELECTOR));
        button.click();
    }

    private static boolean isPageFullyLoaded(WebDriver driver) {
        return AbstractPage.areElementsPresentAndVisible(
                driver,
                SUBMIT_BUTTON_SELECTOR,
                FORM_SELECTOR
        );
    }

    private static boolean isFormFullyLoaded(WebDriver driver) {
        return AbstractPage.areElementsPresentAndVisible(
                driver,
                DEPARTURE_NAME_FIELD_SELECTOR,
                DEPARTURE_ADDRESS_FIELD_SELECTOR,
                DEPARTURE_TIME_FIELD_SELECTOR,
                ARRIVAL_NAME_FIELD_SELECTOR,
                ARRIVAL_ADDRESS_FIELD_SELECTOR,
                ARRIVAL_TIME_FIELD_SELECTOR,
                VEHICLE_FIELD_SELECTOR,
                AVAILABLE_SEATS_FIELD_SELECTOR,
                PRICE_PER_SEAT_FIELD_SELECTOR,
                SUBMIT_BUTTON_SELECTOR
        );
    }

    public boolean isErrorVisibleBySelector(String cssSelector) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return wait.until(driver -> {
                try {
                    WebElement element = driver.findElement(By.cssSelector(cssSelector));
                    return element.isDisplayed() && !element.getText().isBlank();
                } catch (NoSuchElementException e) {
                    return false;
                }
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getGeneralErrorMessage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(ERROR_MESSAGE_SELECTOR)
        ));
        WebElement element = driver.findElement(By.cssSelector(ERROR_MESSAGE_SELECTOR));

        if (element.isDisplayed()) {
            return element.getText();
        }
        return null;
    }


}

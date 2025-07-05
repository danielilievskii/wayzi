package mk.ukim.finki.wayzi.selenium.tests;

import mk.ukim.finki.wayzi.selenium.pages.HomePage;
import mk.ukim.finki.wayzi.selenium.pages.LoginPage;
import mk.ukim.finki.wayzi.selenium.pages.PublishRideFormPage;
import mk.ukim.finki.wayzi.selenium.pages.PublishedRidesPage;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideDto;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideStopDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PublishRideFormPageTest extends BaseTest {

    /**
     * Tests that the user is redirected to the login page when not authenticated.
     */
    @Test
    void shouldRedirectToLoginPage_whenNotAuthenticated() {
        HomePage homePage = HomePage.to(driver);
        assertNotNull(homePage);

        HomePage.clickPublishRideButton(driver);
        assertTrue(LoginPage.isPageFullyLoaded(driver));
    }

    /**
     * Nested test class for testing ride publishing functionality.
     */
    @Nested
    class publishRideTests {

        /**
         * Logs in the user before each test in this nested class.
         */
        @BeforeEach void login() {
            loginAs("daniel@gmail.com", "12345#*lozinka");
        }

        /**
         * Tests the successful publishing of a ride.
         * Verifies that the ride count increases by 1.
         */
        @Test
        void shouldPublishRide() throws InterruptedException {

            PublishedRidesPage initialPage = PublishedRidesPage.to(driver);
            int initialCount = initialPage.getTotalItems();

            LocalDateTime now = LocalDateTime.now();
            CreateRideDto dto = new CreateRideDto(
                    2L, now.plusHours(1), "Address 1",
                    2L, now.plusHours(2), "Address 2",
                    1L, 2, 200, List.of()
            );

            PublishRideFormPage publishRideFormPage = PublishRideFormPage.to(driver);
            assertNotNull(publishRideFormPage);

            PublishRideFormPage.publishRide(driver, dto);
            Thread.sleep(2000);

            PublishedRidesPage afterPage = PublishedRidesPage.to(driver);

            int afterCount = afterPage.getCards().size();
            assertEquals(initialCount + 1, afterCount, "Ride count should increase by 1");
        }

        /**
         * Tests that an error message is displayed when the arrival time is before the departure time.
         */
        @Test
        void shouldShowErrorMessage_whenArrivalTimeBeforeDepartureTime() {
            LocalDateTime now = LocalDateTime.now();
            CreateRideDto dto = new CreateRideDto(
                    2L, now.plusHours(5), "Address 1",
                    2L, now.plusHours(1), "Address 2",
                    1L, 2, 200, List.of()
            );

            PublishRideFormPage publishRideFormPage = PublishRideFormPage.to(driver);
            assertNotNull(publishRideFormPage);

            PublishRideFormPage.publishRide(driver, dto);
            assertEquals("Departure time can't be after arrival time.", publishRideFormPage.getGeneralErrorMessage(driver));
        }

        /**
         * Tests that an error message is displayed when a stop time is outside the departure and arrival time bounds.
         */
        @Test
        void shouldShowErrorMessage_whenStopTimeOutsideTimeBounds() {
            LocalDateTime now = LocalDateTime.now();
            CreateRideDto dto = new CreateRideDto(
                    2L, now.plusHours(2), "Address 1",
                    2L, now.plusHours(5), "Address 2",
                    1L, 2, 200,
                    List.of(
                            new CreateRideStopDto(3L, "Address 3", now.plusHours(10), 1)
                    )
            );

            PublishRideFormPage publishRideFormPage = PublishRideFormPage.to(driver);
            assertNotNull(publishRideFormPage);

            PublishRideFormPage.publishRide(driver, dto);
            assertEquals("Stop time no. 1 must be between departure and arrival time.", publishRideFormPage.getGeneralErrorMessage(driver));
        }

        /**
         * Tests that an error message is displayed when stop times are out of order.
         */
        @Test
        void shouldShowErrorMessage_whenStopTimesOutOfOrder() {
            LocalDateTime now = LocalDateTime.now();
            CreateRideDto dto = new CreateRideDto(
                    1L, now.plusHours(1), "Address 1",
                    2L, now.plusHours(5), "Address 2",
                    1L, 2, 200,
                    List.of(
                            new CreateRideStopDto(3L, "Address 3", now.plusHours(3), 1),
                            new CreateRideStopDto(4L, "Address 4", now.plusHours(2), 2)
                    )
            );

            PublishRideFormPage publishRideFormPage = PublishRideFormPage.to(driver);
            assertNotNull(publishRideFormPage);

            PublishRideFormPage.publishRide(driver, dto);
            assertEquals("Stop no. 1 has later time than stop no. 2", publishRideFormPage.getGeneralErrorMessage(driver));
        }

        /**
         * Tests that all error messages are visible when the form is submitted without data.
         */
        @Test
        void testAllErrorMessagesVisibility() {
            PublishRideFormPage formPage = PublishRideFormPage.to(driver);
            assertNotNull(formPage);

            PublishRideFormPage.submitRide(driver);
            assertTrue(formPage.isErrorVisibleBySelector("#departureLocationError"));
            assertTrue(formPage.isErrorVisibleBySelector("#departureAddressError"));
            assertTrue(formPage.isErrorVisibleBySelector("#arrivalLocationError"));
            assertTrue(formPage.isErrorVisibleBySelector("#arrivalAddressError"));
            assertTrue(formPage.isErrorVisibleBySelector("#availableSeatsError"));
            assertTrue(formPage.isErrorVisibleBySelector("#pricePerSeatError"));

        }
    }







}


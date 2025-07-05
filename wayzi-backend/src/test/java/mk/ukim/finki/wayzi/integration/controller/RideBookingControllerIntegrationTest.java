package mk.ukim.finki.wayzi.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import mk.ukim.finki.wayzi.config.JacksonTestConfig;
import mk.ukim.finki.wayzi.integration.base.AbstractIntegrationTestSetup;
import mk.ukim.finki.wayzi.model.enumeration.PaymentMethod;
import mk.ukim.finki.wayzi.web.dto.ridebooking.CreateRideBookingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(JacksonTestConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class RideBookingControllerIntegrationTest extends AbstractIntegrationTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        initData();
    }

    /**
     * Tests the retrieval of ride bookings.
     * Expects a 200 OK status and verifies the number of ride bookings in the response.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldFindPage() throws Exception {
        mockMvc.perform(get("/api/rides/bookings")
                        .cookie(new Cookie("jwt", tokenUser1))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rideBookings.length()").value(1));
    }

    /**
     * Tests the booking of a ride.
     * Expects a 200 OK status upon successful booking.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldBookRide() throws Exception {
        Long rideId = ride2.getId();

        CreateRideBookingDto dto = new CreateRideBookingDto(
                PaymentMethod.CASH, 1, "Call me when you arrive."
        );
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/rides/{id}/book", rideId)
                        .cookie(new Cookie("jwt", tokenUser1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Tests the cancellation of a ride booking.
     * Expects a 200 OK status upon successful cancellation.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldCancelRideBooking() throws Exception {
        Long rideBookingId = rideBooking2.getId();

        mockMvc.perform(put("/api/rides/bookings/{id}/cancel", rideBookingId)
                        .cookie(new Cookie("jwt", tokenUser1))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Tests the retrieval of booking details for the booker.
     * Expects a 200 OK status and verifies the booking details in the response.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldGetBookingDetailsForBooker() throws Exception {
        Long rideBookingId = rideBooking2.getId();

        mockMvc.perform(get("/api/rides/bookings/{id}", rideBookingId)
                        .cookie(new Cookie("jwt", tokenUser1))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rideBookingId").value(rideBookingId))
                .andExpect(jsonPath("$.driverId").value(rideBooking2.getRide().getDriver().getId()));
    }

    /**
     * Tests the retrieval of booking check-in details for the driver.
     * Expects a 200 OK status and verifies the check-in details in the response.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldGetBookingCheckInDetailsForDriver() throws Exception {
        Long rideBookingId = rideBooking1.getId();

        mockMvc.perform(get("/api/rides/bookings/{id}/check-in", rideBookingId)
                        .cookie(new Cookie("jwt", tokenUser1))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rideBookingId").value(rideBookingId))
                .andExpect(jsonPath("$.bookerId").value(rideBooking1.getBooker().getId()));
    }

    /**
     * Tests the check-in of a passenger for a ride booking.
     * Expects a 200 OK status upon successful check-in.
     * @throws Exception if the request fails.
     */
    @Test
    void shouldCheckInPassenger() throws Exception {
        Long rideBookingId = rideBooking1.getId();

        mockMvc.perform(put("/api/rides/bookings/{id}/check-in", rideBookingId)
                        .cookie(new Cookie("jwt", tokenUser1))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rideBookingId").value(rideBookingId))
                .andExpect(jsonPath("$.bookerId").value(rideBooking1.getBooker().getId()));
    }

}


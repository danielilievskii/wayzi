package mk.ukim.finki.wayzi.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import mk.ukim.finki.wayzi.config.JacksonTestConfig;
import mk.ukim.finki.wayzi.integration.base.AbstractIntegrationTestSetup;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideDto;
import mk.ukim.finki.wayzi.web.dto.ride.UpdateRideDto;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(JacksonTestConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class RideControllerIntegrationTest extends AbstractIntegrationTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
       initData();
    }

    /**
     * Tests the retrieval of rides.
     * Expects a 200 OK status and verifies the number of rides in the response.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldFindPage() throws Exception {
        mockMvc.perform(get("/api/rides"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rides.length()").value(3));
    }

    /**
     * Tests the retrieval of a ride by its ID.
     * Expects a 200 OK status and verifies the ride ID in the response.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldFindById() throws Exception {
        Long rideId = ride1.getId();

        mockMvc.perform(get("/api/rides/{id}", rideId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rideId));
    }

    /**
     * Tests the retrieval of published rides.
     * Expects a 200 OK status and verifies the number of published rides in the response.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldFindPublishedRidesPage() throws Exception {
        mockMvc.perform(get("/api/rides/published")
                        .cookie(new Cookie("jwt", tokenUser1))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rides.length()").value(2));
    }

    /**
     * Tests the retrieval of bookers for a specific ride by its ID.
     * Expects a 200 OK status and verifies the ride ID in the response.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldFindBookersById() throws Exception {
        Long rideId = ride1.getId();

        mockMvc.perform(get("/api/rides/published/{id}/bookings", rideId)
                        .cookie(new Cookie("jwt", tokenUser1))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rideId));
    }

    /**
     * Tests the creation of a new ride.
     * Expects a 200 OK status upon successful creation.
     * @throws Exception if the request fails.
     */
    @Test
    void shouldCreateRide() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Long vehicleId = vehicle1.getId();
        Long bitolaId = skopje.getId();
        Long skopjeId = bitola.getId();

        CreateRideDto createDto = new CreateRideDto(
                skopjeId, now.plusHours(1), "Address 1",
                bitolaId, now.plusHours(2), "Address 2",
                vehicleId, 2, 200, List.of()
        );
        String json = objectMapper.writeValueAsString(createDto);

        mockMvc.perform(post("/api/rides")
                        .cookie(new Cookie("jwt", tokenUser1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Tests the update of an existing ride.
     * Expects a 200 OK status upon successful update.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldUpdateRide() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Long rideId = ride1.getId();
        Long vehicleId = vehicle1.getId();
        Long bitolaId = skopje.getId();
        Long skopjeId = bitola.getId();

        UpdateRideDto updateRideDto = new UpdateRideDto(skopjeId, now.plusHours(1), "Address 1",
                bitolaId, now.plusHours(2), "Address 2",
                vehicleId, 2, 200, List.of()
        );
        String json = objectMapper.writeValueAsString(updateRideDto);

        mockMvc.perform(put("/api/rides/published/{id}/edit", rideId)
                .cookie(new Cookie("jwt", tokenUser1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }



}

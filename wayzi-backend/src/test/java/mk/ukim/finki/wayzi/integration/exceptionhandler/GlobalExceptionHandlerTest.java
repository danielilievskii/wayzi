package mk.ukim.finki.wayzi.integration.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import mk.ukim.finki.wayzi.config.JacksonTestConfig;
import mk.ukim.finki.wayzi.integration.base.AbstractIntegrationTestSetup;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import mk.ukim.finki.wayzi.web.dto.auth.SignUpDto;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideDto;
import mk.ukim.finki.wayzi.web.dto.ride.UpdateRideStatusDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JacksonTestConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class GlobalExceptionHandlerTest extends AbstractIntegrationTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
       initData();
    }

    /**
     * Tests that a 400 Bad Request status is returned when IllegalArgumentException is thrown.
     * @throws Exception if the request fails.
     */
    @Test
    void shouldReturn400_IllegalArgumentExceptionThrown() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Long vehicleId = vehicle1.getId();
        Long bitolaId = skopje.getId();
        Long skopjeId = bitola.getId();

        CreateRideDto createDto = new CreateRideDto(
                bitolaId, now.minusHours(1), "Address 1",
                skopjeId, now.plusHours(2), "Address 2",
                vehicleId, 2, 200, List.of()
        );
        String json = objectMapper.writeValueAsString(createDto);

        mockMvc.perform(post("/api/rides")
                        .cookie(new Cookie("jwt", tokenUser1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests that a 400 Bad Request status is returned when IllegalStateException is thrown.
     * @throws Exception if the request fails.
     */
    @Test
    void shouldReturn400_IllegalStateExceptionThrown() throws Exception {
        Long id = ride1.getId();

        UpdateRideStatusDto dto = new UpdateRideStatusDto(id, RideStatus.PENDING);
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/api/rides/update-status")
                        .cookie(new Cookie("jwt", tokenUser1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests that a 404 Not Found status is returned when ResourceNotFoundException is thrown.
     * @throws Exception if the request fails.
     */
    @Test
    void shouldReturn404_ResourceNotFoundExceptionThrown() throws Exception {
        Long id = 10000L;

        mockMvc.perform(get("/api/rides/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * Tests that a 409 Conflict status is returned when ConflictException is thrown.
     * @throws Exception if the request fails.
     */
    @Test
    void shouldReturn409_ConflictExceptionThrown() throws Exception {
        SignUpDto signUpDto = new SignUpDto("Petko", "daniel@gmail.com", "54321", "54321");
        String json = objectMapper.writeValueAsString(signUpDto);

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                )
                .andDo(print())
                .andExpect(status().isConflict());
    }
}

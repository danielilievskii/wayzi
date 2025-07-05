package mk.ukim.finki.wayzi.integration.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import mk.ukim.finki.wayzi.config.JacksonTestConfig;
import mk.ukim.finki.wayzi.integration.base.AbstractIntegrationTestSetup;
import mk.ukim.finki.wayzi.web.dto.auth.SignInDto;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideDto;
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
public class AuthExceptionHandlerTest extends AbstractIntegrationTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        initData();
    }

    /**
     * Tests that a 403 Forbidden status is returned when AccessDeniedException is thrown.
     * @throws Exception if the request fails.
     */
    @Test
    void shouldReturn403_whenAccessDeniedExceptionThrown() throws Exception {
        Long id = vehicle2.getId();

        mockMvc.perform(get("/api/vehicles/{id}", id)
                        .cookie(new Cookie("jwt", tokenUser1)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    /**
     * Tests that a 401 Unauthorized status is returned when BadCredentialsException is thrown.
     * @throws Exception if the request fails.
     */
    @Test
    void shouldReturn401_whenBadCredentialsExceptionThrown() throws Exception {
        SignInDto dto = new SignInDto("daniel@gmail.com", "123456");
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests that a 401 Unauthorized status is returned when AuthenticationException is thrown.
     * @throws Exception if the request fails.
     */
    @Test
    void shouldReturn401_whenAuthenticationExceptionThrown() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Long vehicleId = vehicle1.getId();
        Long bitolaId = skopje.getId();
        Long skopjeId = bitola.getId();

        CreateRideDto createDto = new CreateRideDto(
                bitolaId, now.plusHours(1), "Address 1",
                skopjeId, now.plusHours(3), "Address 2",
                vehicleId, 2, 200, List.of()
        );
        String json = objectMapper.writeValueAsString(createDto);

        mockMvc.perform(post("/api/rides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests that a 401 Unauthorized status is returned when SignatureException is thrown.
     * @throws Exception if the request fails.
     */
    @Test
    void shouldReturn401_whenSignatureExceptionThrown() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Long vehicleId = vehicle1.getId();
        Long bitolaId = skopje.getId();
        Long skopjeId = bitola.getId();

        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYW5pZWwiLCJleHAiOjQ3MDE4MzU2MDAsImlhdCI6MTcxOTYyOTYwMH0.R9E1NKsMUNccIycAePELGH_Ck9cHJe2y2Qakf3pAwDo";

        CreateRideDto createDto = new CreateRideDto(
                bitolaId, now.plusHours(1), "Address 1",
                skopjeId, now.plusHours(3), "Address 2",
                vehicleId, 2, 200, List.of()
        );
        String json = objectMapper.writeValueAsString(createDto);

        mockMvc.perform(post("/api/rides")
                        .cookie(new Cookie("jwt", invalidToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


}

package mk.ukim.finki.wayzi.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import mk.ukim.finki.wayzi.config.JacksonTestConfig;
import mk.ukim.finki.wayzi.integration.base.AbstractIntegrationTestSetup;
import mk.ukim.finki.wayzi.web.dto.auth.SignInDto;
import mk.ukim.finki.wayzi.web.dto.auth.SignUpDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(JacksonTestConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class AuthControllerIntegrationTest extends AbstractIntegrationTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        initData();
    }

    /**
     * Tests the user sign-up functionality.
     * Expects a 200 OK status upon successful sign-up.
     * @throws Exception if the request fails.
     */
    @Test
    void shouldSignUp() throws Exception {
        SignUpDto dto = new SignUpDto(
                "Daniel", "daniel3@gmail.com", "12345", "12345"
        );
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Tests the user sign-in functionality.
     * Expects a 200 OK status and verifies the user ID in the response.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldSignIn() throws Exception {
        SignInDto dto = new SignInDto(
                "daniel@gmail.com", "12345#*lozinka"
        );
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1.getId()));
    }

    /**
     * Tests the user sign-out functionality.
     * Expects a 200 OK status and verifies the JWT cookie is invalidated.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldSignOut() throws Exception {
        SignInDto dto = new SignInDto(
                "daniel@gmail.com", "12345#*lozinka"
        );
        String json = objectMapper.writeValueAsString(dto);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        Cookie jwtCookie = loginResult.getResponse().getCookie("jwt");
        assertNotNull(jwtCookie);

        mockMvc.perform(post("/api/auth/signout")
                        .cookie(new Cookie("jwt", jwtCookie.getValue()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("jwt", 0));
    }

    /**
     * Tests the retrieval of the currently authenticated user's details.
     * Expects a 200 OK status and verifies the user ID in the response.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldReturnMe() throws Exception {
        mockMvc.perform(get("/api/auth/me")
                        .cookie(new Cookie("jwt", tokenUser1))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1.getId()));
    }


}

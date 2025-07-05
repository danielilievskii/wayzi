package mk.ukim.finki.wayzi.integration.controller;

import jakarta.servlet.http.Cookie;
import mk.ukim.finki.wayzi.config.JacksonTestConfig;
import mk.ukim.finki.wayzi.integration.base.AbstractIntegrationTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
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
public class UserControllerIntegrationTest extends AbstractIntegrationTestSetup {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        initData();
    }

    /**
     * Tests the upload of a user's profile picture.
     * Expects a 200 OK status upon successful upload.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldUploadProfilePicture() throws Exception {

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "profile.png",
                MediaType.IMAGE_PNG_VALUE,
                "image".getBytes()
        );

        mockMvc.perform(multipart("/api/user/submit-profile-pic")
                        .file(mockFile)
                        .cookie(new Cookie("jwt", tokenUser1))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk());
    }

    /**
     * Tests the download of a user's profile picture.
     * Expects a 200 OK status and verifies the content type of the response.
     * @throws Exception if the request fails.
     */
    @Test
    public void shouldDownloadProfilePicture() throws Exception {
        mockMvc.perform(get("/api/user/" + user1.getId() + "/download-profile-pic")
                        .cookie(new Cookie("jwt", tokenUser1))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
    }


}

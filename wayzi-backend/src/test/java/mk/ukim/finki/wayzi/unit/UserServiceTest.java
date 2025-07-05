package mk.ukim.finki.wayzi.unit;

import mk.ukim.finki.wayzi.model.domain.User;
import mk.ukim.finki.wayzi.model.exception.ResourceNotFoundException;
import mk.ukim.finki.wayzi.repository.UserRepository;
import mk.ukim.finki.wayzi.service.domain.AuthService;
import mk.ukim.finki.wayzi.service.domain.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private MultipartFile multipartFile;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("daniel@gmail.com", "12345", "Daniel", true);
        user.setId(1L);
    }

    /**
     * Nested test class for testing the submitProfilePic method.
     */
    @Nested
    class SubmitProfilePic {

        @BeforeEach
        void setUp() {
            when(authService.getAuthenticatedUser()).thenReturn(user);
        }

        /**
         * Tests the successful submission of a profile picture.
         * Verifies that the profile picture is saved correctly.
         * @throws Exception if an error occurs during the test.
         */
        @Test
        void shouldSubmitProfilePic() throws Exception {
            byte[] mockImage = "testImageData".getBytes();
            when(multipartFile.getBytes()).thenReturn(mockImage);

            userService.submitProfilePic(multipartFile);

            assertArrayEquals(mockImage, user.getProfilePic());
            verify(userRepository).save(user);
        }

        /**
         * Tests that an exception is thrown when an IOException occurs during profile picture submission.
         * Verifies that the exception message contains the expected error.
         * @throws Exception if an error occurs during the test.
         */
        @Test
        void shouldThrowRuntimeException_whenIOExceptionThrown() throws Exception {
            when(multipartFile.getBytes()).thenThrow(new IOException("Failed"));

            RuntimeException ex = assertThrows(RuntimeException.class, () ->
                    userService.submitProfilePic(multipartFile)
            );

            assertTrue(ex.getMessage().contains("Failed to upload profile picture"));
        }
    }

    /**
     * Nested test class for testing the loadProfilePicAsResource method.
     */
    @Nested
    class LoadProfilePicAsResource {

        /**
         * Tests the successful loading of a profile picture as a Resource.
         * Verifies that the returned Resource contains the correct data.
         */
        @Test
        void shouldLoadProfilePicAsResource() {
            byte[] mockImage = "testImageData".getBytes();
            user.setProfilePic(mockImage);

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            Resource resource = userService.loadProfilePicAsResource(1L);

            assertTrue(resource instanceof ByteArrayResource);
            assertArrayEquals(mockImage, ((ByteArrayResource) resource).getByteArray());
        }

        /**
         * Tests that an exception is thrown when the user is not found.
         * Verifies that the exception is of the expected type.
         */
        @Test
        void shouldThrowRuntimeException_whenUserNotFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> userService.loadProfilePicAsResource(1L));
        }

        /**
         * Tests that an exception is thrown when the profile picture is null.
         * Verifies that the exception is of the expected type.
         */
        @Test
        void shouldThrowResourceNotFoundException_whenPictureIsNull() {
            user.setProfilePic(null);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThrows(ResourceNotFoundException.class, () ->
                    userService.loadProfilePicAsResource(1L));
        }

        /**
         * Tests that an exception is thrown when the profile picture is empty.
         * Verifies that the exception is of the expected type.
         */
        @Test
        void shouldThrowResourceNotFoundException_whenPictureIsEmpty() {
            user.setProfilePic(new byte[0]);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            assertThrows(ResourceNotFoundException.class, () ->
                    userService.loadProfilePicAsResource(1L));
        }
    }
}

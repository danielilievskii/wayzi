package mk.ukim.finki.wayzi.unit;

import jakarta.servlet.http.Cookie;
import mk.ukim.finki.wayzi.model.domain.User;
import mk.ukim.finki.wayzi.model.exception.AuthenticationFailedException;
import mk.ukim.finki.wayzi.model.exception.ConflictException;
import mk.ukim.finki.wayzi.repository.UserRepository;
import mk.ukim.finki.wayzi.service.domain.JwtService;
import mk.ukim.finki.wayzi.service.domain.VerificationTokenService;
import mk.ukim.finki.wayzi.service.domain.impl.AuthServiceImpl;
import mk.ukim.finki.wayzi.web.dto.auth.SignInDto;
import mk.ukim.finki.wayzi.web.dto.auth.SignUpDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private VerificationTokenService verificationTokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    private User user;

    @BeforeEach
    void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        user = new User("daniel@gmail.com", passwordEncoder.encode("12345#*lozinka"), "Daniel", true);
    }

    /**
     * Nested test class for testing the signUp method.
     */
    @Nested
    class SignUp {

        /**
         * Tests the successful registration of a new user and creation of a verification token.
         */
        @Test
        void shouldRegisterNewUserAndCreateVerificationToken() {
            SignUpDto signUpDto = new SignUpDto(
                    "Petko","petko@gmail.com", "12345", "12345"
            );
            when(userRepository.existsByEmail(signUpDto.email())).thenReturn(false);
            when(passwordEncoder.encode(signUpDto.password())).thenReturn("encodedPassword");

            User result = authService.signUp(signUpDto, request, response);

            assertEquals(signUpDto.email(), result.getEmail());
            assertEquals("encodedPassword", result.getPassword());
            assertEquals(signUpDto.name(), result.getName());
            assertFalse(result.isEnabled());

            verify(userRepository).save(any(User.class));
            verify(verificationTokenService).createVerificationToken(result);
        }

        /**
         * Tests that a ConflictException is thrown when the user already exists.
         */
        @Test
        void shouldThrowConflictException_whenUserAlreadyExists() {
            SignUpDto signUpDto = new SignUpDto(
                    "Petko","petko@gmail.com", "12345", "12345"
            );
            when(userRepository.existsByEmail(signUpDto.email())).thenReturn(true);

            assertThrows(ConflictException.class,
                    () -> authService.signUp(signUpDto, request, response));
        }
    }

    /**
     * Nested test class for testing the signIn method.
     */
    @Nested
    class SignIn {

        /**
         * Tests the successful authentication of a user and generation of a JWT token.
         */
        @Test
        void shouldAuthenticateAndReturnUser() {
            SignInDto signInDto = new SignInDto("daniel@gmail.com", "12345#*lozinka");
            String token = "token";

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

            when(jwtService.generateToken(user)).thenReturn(token);

            User result = authService.signIn(signInDto, request, response);

            Cookie jwtCookie = Arrays.stream(response.getCookies())
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);

            assertEquals(user, result);
            assertEquals(token, jwtCookie.getValue());
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService).generateToken(user);
        }

        /**
         * Tests that a BadCredentialsException is thrown when authentication fails.
         */
        @Test
        void shouldThrowBadCredentialsException_onAuthenticationFailure() {
            SignInDto signInDto = new SignInDto("daniel@gmail.com", "password");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(BadCredentialsException.class);

            BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> authService.signIn(signInDto, request, response));
            assertEquals(("Invalid email or password."), exception.getMessage());
        }

        /**
         * Tests that an AuthenticationFailedException is thrown on a generic error during authentication.
         */
        @Test
        void shouldThrowAuthenticationFailedException_onGenericError() {
            SignInDto signInDto = new SignInDto("daniel@gmail.com", "12345#*lozinka");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(new UsernamePasswordAuthenticationToken("daniel@gmail.com", null, List.of()));

            when(userRepository.findByEmail(signInDto.email()))
                    .thenThrow(new RuntimeException("Database error"));


            AuthenticationFailedException exception = assertThrows(AuthenticationFailedException.class,
                    () -> authService.signIn(signInDto, request, response));

            assertEquals("An error occurred during login.", exception.getMessage());
        }

        /**
         * Tests that a UsernameNotFoundException is thrown when the user is not found.
         */
        @Test
        void shouldThrowUsernameNotFoundException() {
            SignInDto signInDto = new SignInDto("daniel@gmail.com", "12345#*lozinka");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

            when(userRepository.findByEmail(signInDto.email()))
                    .thenThrow(new UsernameNotFoundException("User not found"));

            UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                    () -> authService.signIn(signInDto, request, response));
            assertEquals("User not found", exception.getMessage());

        }
    }
}


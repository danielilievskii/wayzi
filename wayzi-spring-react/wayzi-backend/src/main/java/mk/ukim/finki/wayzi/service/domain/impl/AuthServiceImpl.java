package mk.ukim.finki.wayzi.service.domain.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wayzi.dto.SignInDto;
import mk.ukim.finki.wayzi.dto.SignUpDto;
import mk.ukim.finki.wayzi.model.exception.AuthenticationException;
import mk.ukim.finki.wayzi.model.exception.AuthenticationFailedException;
import mk.ukim.finki.wayzi.model.exception.InvalidCredentialsException;
import mk.ukim.finki.wayzi.model.exception.UserAlreadyExistsException;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.repository.StandardUserRepository;
import mk.ukim.finki.wayzi.repository.UserRepository;
import mk.ukim.finki.wayzi.service.domain.AuthService;
import mk.ukim.finki.wayzi.service.domain.JwtService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final StandardUserRepository standardUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(@Lazy AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtService jwtService, UserRepository userRepository, StandardUserRepository standardUserRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.standardUserRepository = standardUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User signUp(SignUpDto signUpDto, HttpServletRequest request, HttpServletResponse response) {
        if (userRepository.existsByEmail(signUpDto.email())) {
            throw new UserAlreadyExistsException("A user with this email already exists.");
        }

        StandardUser standardUser = standardUserRepository.save(signUpDto.toEntity(passwordEncoder));
        authenticateAndSetJwt(request, response, standardUser);

        return standardUser;
    }

    @Override
    public User signIn(SignInDto signInDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInDto.email(), signInDto.password())
            );

            StandardUser standardUser = standardUserRepository.findByEmail(signInDto.email());
            authenticateAndSetJwt(request, response, standardUser);

            return standardUser;

        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid email or password.");
        } catch (Exception e) {
            throw new AuthenticationFailedException("An error occurred during login.");
        }
    }

    @Override
    public void signOut(HttpServletResponse response) {
        invalidateJwtCookie(response);
        clearAuthentication();
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        String jwt = getJwtFromCookies(request);

        if (jwt == null) {
            throw new AuthenticationException("Not authenticated");
        }

        String userEmail = jwtService.extractUsername(jwt);
        if (userEmail == null) {
            throw new AuthenticationException("Invalid token");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user;
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public StandardUser getAuthenticatedStandardUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        return standardUserRepository.findByEmail(username);
    }

    private void authenticateAndSetJwt(HttpServletRequest request, HttpServletResponse response, User user) {
        // Generate a JWT token for the authenticated user
        String jwt = jwtService.generateToken(user);

        // Add the generated JWT token as a cookie in the response
        addJwtCookie(response, jwt);

        // Set the authentication in the security context for the current session
        setAuthentication(user, request);
    }

    /**
     * Sets authentication in the SecurityContext.
     */
    @Override
    public void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    /**
     * Clears authentication in the SecurityContext.
     */
    @Override
    public void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Extracts JWT token from cookies.
     */
    @Override
    public String getJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Creates and attaches a secure HTTP-only JWT cookie.
     */
    @Override
    public void addJwtCookie(HttpServletResponse response, String jwt) {
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Enable only in HTTPS environments
        cookie.setPath("/"); // Available across the application
        cookie.setMaxAge(900); // 15 minutes expiration

        response.addCookie(cookie);
    }

    /**
     * Invalidates the JWT cookie by setting it to expire immediately.
     */
    public void invalidateJwtCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Enable only in HTTPS environments
        cookie.setPath("/"); // Available across the application
        cookie.setMaxAge(0); // Expire immediately

        response.addCookie(cookie);
    }


}

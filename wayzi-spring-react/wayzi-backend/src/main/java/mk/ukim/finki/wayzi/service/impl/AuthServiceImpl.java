package mk.ukim.finki.wayzi.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wayzi.dto.SignInDto;
import mk.ukim.finki.wayzi.dto.SignUpDto;
import mk.ukim.finki.wayzi.dto.AuthUserDto;
import mk.ukim.finki.wayzi.exception.AuthenticationException;
import mk.ukim.finki.wayzi.exception.AuthenticationFailedException;
import mk.ukim.finki.wayzi.exception.InvalidCredentialsException;
import mk.ukim.finki.wayzi.exception.UserAlreadyExistsException;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.repository.StandardUserRepository;
import mk.ukim.finki.wayzi.repository.UserRepository;
import mk.ukim.finki.wayzi.service.AuthService;
import mk.ukim.finki.wayzi.service.JwtService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public AuthUserDto signUp(SignUpDto signUpDto, HttpServletRequest request, HttpServletResponse response) {
        if (userRepository.existsByEmail(signUpDto.email())) {
            throw new UserAlreadyExistsException("A user with this email already exists.");
        }

        StandardUser standardUser = standardUserRepository.save(signUpDto.toEntity(passwordEncoder));
        return authenticateAndReturnDto(standardUser, request, response);

    }

    @Override
    public AuthUserDto signIn(SignInDto signInDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInDto.email(), signInDto.password())
            );

            StandardUser standardUser = standardUserRepository.findByEmail(signInDto.email());
            return authenticateAndReturnDto(standardUser, request, response);

        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }  catch (Exception e) {
            throw new AuthenticationFailedException("An error occurred during login.");
        }
    }

    @Override
    public void signOut(HttpServletResponse response) {
        invalidateJwtCookie(response);
        clearAuthentication();
    }

    @Override
    public AuthUserDto getCurrentUser(HttpServletRequest request) {
        String jwt = getJwtFromCookies(request);

        if(jwt == null) {
            throw new AuthenticationException("Not authenticated");
        }

        String userEmail = jwtService.extractUsername(jwt);
        if(userEmail == null) {
            throw new AuthenticationException("Invalid token");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new AuthUserDto(user.getId(), user.getEmail(), user.getName(), user.getRole().getAuthority());
    }

    private AuthUserDto authenticateAndReturnDto(StandardUser standardUser, HttpServletRequest request, HttpServletResponse response) {
        String jwt = jwtService.generateToken(standardUser);
        addJwtCookie(response, jwt);
        setAuthentication(standardUser, request);

        return new AuthUserDto(standardUser.getId(), standardUser.getEmail(), standardUser.getName(), standardUser.getRole().getAuthority());
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

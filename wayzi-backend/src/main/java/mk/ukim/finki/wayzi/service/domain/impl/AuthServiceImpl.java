package mk.ukim.finki.wayzi.service.domain.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import mk.ukim.finki.wayzi.model.exception.ConflictException;
import mk.ukim.finki.wayzi.service.domain.VerificationTokenService;
import mk.ukim.finki.wayzi.web.dto.auth.SignInDto;
import mk.ukim.finki.wayzi.web.dto.auth.SignUpDto;
import mk.ukim.finki.wayzi.model.exception.AuthenticationException;
import mk.ukim.finki.wayzi.model.exception.AuthenticationFailedException;
import mk.ukim.finki.wayzi.model.domain.User;
import mk.ukim.finki.wayzi.repository.UserRepository;
import mk.ukim.finki.wayzi.service.domain.AuthService;
import mk.ukim.finki.wayzi.service.domain.JwtService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;

    public AuthServiceImpl(@Lazy AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder, VerificationTokenService verificationTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenService = verificationTokenService;
    }

    @Override
    public User signUp(SignUpDto signUpDto, HttpServletRequest request, HttpServletResponse response) {
        if (userRepository.existsByEmail(signUpDto.email())) {
            throw new ConflictException("User with this email already exists.");
        }

        User user = new User(signUpDto.email(), passwordEncoder.encode(signUpDto.password()), signUpDto.name(), false);
        userRepository.save(user);

        verificationTokenService.createVerificationToken(user);

        return user;
    }

    @Override
    public User signIn(SignInDto signInDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInDto.email(), signInDto.password())
            );

            User user = userRepository.findByEmail(signInDto.email())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            authenticateAndSetJwt(response, user);

            return user;

        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password.");
        } catch (Exception e) {
            throw new AuthenticationFailedException("An error occurred during login.");
        }
    }

    @Override
    public void signOut(HttpServletResponse response) {
        invalidateJwtCookie(response);
    }

    @Override
    public void verifyEmail(String token) {
        verificationTokenService.validateToken(token);
    }

    @Override
    @Transactional
    public User getCurrentUser(HttpServletRequest request) {
        String jwt = getJwtFromCookies(request);
        if (jwt == null) {
            throw new AuthenticationException("Not authenticated");
        }

        String userEmail = jwtService.extractUsername(jwt);
        if (userEmail == null) {
            throw new AuthenticationException("Invalid token");
        }

        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationException("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private void authenticateAndSetJwt(HttpServletResponse response, User user) {
        // Generate a JWT token for the authenticated user
        String jwt = jwtService.generateToken(user);

        // Add the generated JWT token as a cookie in the response
        addJwtCookie(response, jwt);
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
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }

    /**
     * Invalidates the JWT cookie by setting it to expire immediately.
     */
    public void invalidateJwtCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Expire immediately

        response.addCookie(cookie);
    }


}

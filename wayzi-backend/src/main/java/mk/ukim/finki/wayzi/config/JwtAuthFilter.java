package mk.ukim.finki.wayzi.config;

import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wayzi.service.domain.AuthService;
import mk.ukim.finki.wayzi.service.domain.JwtService;
import mk.ukim.finki.wayzi.service.domain.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthService authService;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService, AuthService authService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = authService.getJwtFromCookies(request);

        try {
            if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String userEmail = jwtService.extractUsername(jwt);

                if (userEmail != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        authService.setAuthentication(userDetails, request);
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (SignatureException ex) {
            // Delegate to HandlerExceptionResolver to handle response
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }


}

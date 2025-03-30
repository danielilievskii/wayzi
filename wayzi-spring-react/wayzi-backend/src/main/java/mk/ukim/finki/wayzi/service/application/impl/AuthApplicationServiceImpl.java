package mk.ukim.finki.wayzi.service.application.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wayzi.dto.AuthUserDto;
import mk.ukim.finki.wayzi.dto.SignInDto;
import mk.ukim.finki.wayzi.dto.SignUpDto;
import mk.ukim.finki.wayzi.service.application.AuthApplicationService;
import mk.ukim.finki.wayzi.service.domain.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthApplicationServiceImpl implements AuthApplicationService {

    private final AuthService authService;

    public AuthApplicationServiceImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public AuthUserDto signUp(SignUpDto signUpDto, HttpServletRequest request, HttpServletResponse response) {
        return AuthUserDto.from(authService.signUp(signUpDto, request, response));
    }

    @Override
    public AuthUserDto signIn(SignInDto signInDto, HttpServletRequest request, HttpServletResponse response) {
        return AuthUserDto.from(authService.signIn(signInDto, request, response));
    }

    @Override
    public void signOut(HttpServletResponse response) {
        authService.signOut(response);
    }

    @Override
    public AuthUserDto getAuthenticatedUser() {
        return AuthUserDto.from(authService.getAuthenticatedUser());
    }
}

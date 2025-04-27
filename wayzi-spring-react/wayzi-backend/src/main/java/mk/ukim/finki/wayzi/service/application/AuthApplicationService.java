package mk.ukim.finki.wayzi.service.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wayzi.web.dto.AuthUserDto;
import mk.ukim.finki.wayzi.web.dto.SignInDto;
import mk.ukim.finki.wayzi.web.dto.SignUpDto;

public interface AuthApplicationService {
    AuthUserDto signUp(SignUpDto signUpDto, HttpServletRequest request, HttpServletResponse response);
    AuthUserDto signIn(SignInDto signInDto, HttpServletRequest request, HttpServletResponse response);
    void signOut(HttpServletResponse response);
    AuthUserDto getAuthenticatedUser();
}

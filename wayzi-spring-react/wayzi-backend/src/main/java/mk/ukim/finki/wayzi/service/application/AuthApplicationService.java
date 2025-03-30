package mk.ukim.finki.wayzi.service.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wayzi.dto.AuthUserDto;
import mk.ukim.finki.wayzi.dto.SignInDto;
import mk.ukim.finki.wayzi.dto.SignUpDto;
import mk.ukim.finki.wayzi.model.domain.user.User;

public interface AuthApplicationService {
    AuthUserDto signUp(SignUpDto signUpDto, HttpServletRequest request, HttpServletResponse response);
    AuthUserDto signIn(SignInDto signInDto, HttpServletRequest request, HttpServletResponse response);
    void signOut(HttpServletResponse response);
    AuthUserDto getAuthenticatedUser();
}

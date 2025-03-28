package mk.ukim.finki.wayzi.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wayzi.dto.SignInDto;
import mk.ukim.finki.wayzi.dto.SignUpDto;
import mk.ukim.finki.wayzi.dto.AuthUserDto;
import mk.ukim.finki.wayzi.model.domain.user.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    AuthUserDto signUp(SignUpDto signUpDto, HttpServletRequest request, HttpServletResponse response);
    AuthUserDto signIn(SignInDto signInDto, HttpServletRequest request, HttpServletResponse response);
    void signOut(HttpServletResponse response);

    AuthUserDto getCurrentUser(HttpServletRequest request);


    String getJwtFromCookies(HttpServletRequest request);
    void addJwtCookie(HttpServletResponse response, String jwt);
    void invalidateJwtCookie(HttpServletResponse response);
    void setAuthentication(UserDetails userDetails, HttpServletRequest request);
    void clearAuthentication();
}

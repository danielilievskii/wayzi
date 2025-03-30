package mk.ukim.finki.wayzi.service.domain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wayzi.dto.SignInDto;
import mk.ukim.finki.wayzi.dto.SignUpDto;
import mk.ukim.finki.wayzi.dto.AuthUserDto;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.user.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {
    User signUp(SignUpDto signUpDto, HttpServletRequest request, HttpServletResponse response);
    User signIn(SignInDto signInDto, HttpServletRequest request, HttpServletResponse response);
    void signOut(HttpServletResponse response);

    User getCurrentUser(HttpServletRequest request);
    User getAuthenticatedUser();
    StandardUser getAuthenticatedStandardUser();


    String getJwtFromCookies(HttpServletRequest request);
    void addJwtCookie(HttpServletResponse response, String jwt);
    void invalidateJwtCookie(HttpServletResponse response);
    void setAuthentication(UserDetails userDetails, HttpServletRequest request);
    void clearAuthentication();
}

package mk.ukim.finki.wayzi.service;

import mk.ukim.finki.wayzi.dto.SignUpDto;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.user.User;

public interface AuthService {

    User signUp(SignUpDto signUpDto);

    User getAuthenticatedUser();
    StandardUser getAuthenticatedStandardUser();
    Long getAuthenticatedUserId();
}

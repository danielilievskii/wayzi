package mk.ukim.finki.wayzi.model.exception;

import mk.ukim.finki.wayzi.model.exception.base.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProfilePictureNotFoundException extends EntityNotFoundException {
    public ProfilePictureNotFoundException(Long userId) {
        super(String.format("Profile picture for the user with id: %d was not found", userId));
    }
}

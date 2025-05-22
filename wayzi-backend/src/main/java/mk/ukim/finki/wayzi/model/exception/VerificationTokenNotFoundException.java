package mk.ukim.finki.wayzi.model.exception;

import mk.ukim.finki.wayzi.model.exception.base.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VerificationTokenNotFoundException extends EntityNotFoundException {
    public VerificationTokenNotFoundException(String message) {
        super(message);
    }
}

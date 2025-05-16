package mk.ukim.finki.wayzi.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PassengerCheckInNotAllowedException extends RuntimeException {
    public PassengerCheckInNotAllowedException(String message) {
        super(message);
    }
}

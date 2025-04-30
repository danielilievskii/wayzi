package mk.ukim.finki.wayzi.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RideBookingNotAllowedException extends RuntimeException {
    public RideBookingNotAllowedException(String message) {
        super(message);
    }
}

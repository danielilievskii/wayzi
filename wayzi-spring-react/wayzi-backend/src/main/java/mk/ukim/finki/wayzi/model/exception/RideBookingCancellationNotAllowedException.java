package mk.ukim.finki.wayzi.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RideBookingCancellationNotAllowedException extends RuntimeException {
    public RideBookingCancellationNotAllowedException(String message) {
        super(message);
    }
}

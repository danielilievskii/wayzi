package mk.ukim.finki.wayzi.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RideBookingNotFoundException extends RuntimeException {
    public RideBookingNotFoundException(Long id) {
        super(String.format("Ride booking with id: %d was not found", id));
    }
}

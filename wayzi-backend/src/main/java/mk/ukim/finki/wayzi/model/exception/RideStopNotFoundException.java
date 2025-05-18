package mk.ukim.finki.wayzi.model.exception;

import mk.ukim.finki.wayzi.model.exception.base.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RideStopNotFoundException extends EntityNotFoundException {
    public RideStopNotFoundException(Long id) {
        super(String.format("Ride stop with id: %d was not found", id));
    }
}

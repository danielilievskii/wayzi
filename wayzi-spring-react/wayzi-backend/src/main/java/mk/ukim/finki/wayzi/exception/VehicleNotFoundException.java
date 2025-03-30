package mk.ukim.finki.wayzi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(Long id) {
        super(String.format("Vehicle with id: %d was not found", id));
    }
}

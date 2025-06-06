package mk.ukim.finki.wayzi.model.exception;

import mk.ukim.finki.wayzi.model.exception.base.BadRequestException;
import mk.ukim.finki.wayzi.model.exception.base.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RideBadRequestException extends BadRequestException {
    public RideBadRequestException(String message) {
        super(message);
    }
}

package mk.ukim.finki.wayzi.model.exception;

import mk.ukim.finki.wayzi.model.exception.base.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PassengerCheckInNotAllowedException extends BadRequestException {
    public PassengerCheckInNotAllowedException(String message) {
        super(message);
    }
}

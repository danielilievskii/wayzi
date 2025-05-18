package mk.ukim.finki.wayzi.model.exception;

import mk.ukim.finki.wayzi.model.exception.base.BadRequestException;

public class InvalidRideStatusException extends BadRequestException {
    public InvalidRideStatusException(String message) {
        super(message);
    }
}

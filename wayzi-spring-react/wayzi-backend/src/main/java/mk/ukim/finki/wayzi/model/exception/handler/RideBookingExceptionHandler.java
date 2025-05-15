package mk.ukim.finki.wayzi.model.exception.handler;

import mk.ukim.finki.wayzi.model.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RideBookingExceptionHandler {

    @ExceptionHandler(RideBookingNotFoundException.class)
    public ResponseEntity<String> handleRideBookingNotFoundException(RideBookingNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(RideBookingNotAllowedException.class)
    public ResponseEntity<String> handleRideBookingNotAllowedException(RideBookingNotAllowedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(RideBookingCancellationNotAllowedException.class)
    public ResponseEntity<String> handleRideBookingCancellationNotAllowedException(RideBookingCancellationNotAllowedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(PassengerCheckInNotAllowedException.class)
    public ResponseEntity<String> handlePassengerCheckInNotAllowedExceptionException(PassengerCheckInNotAllowedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

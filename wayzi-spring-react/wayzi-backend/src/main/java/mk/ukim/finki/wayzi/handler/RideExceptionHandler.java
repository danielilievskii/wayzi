package mk.ukim.finki.wayzi.handler;

import mk.ukim.finki.wayzi.exception.InvalidRideStatusException;
import mk.ukim.finki.wayzi.exception.RideNotFoundException;
import mk.ukim.finki.wayzi.exception.RideStopNotFoundException;
import mk.ukim.finki.wayzi.exception.VehicleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RideExceptionHandler {

    @ExceptionHandler(RideNotFoundException.class)
    public ResponseEntity<String> handleRideNotFoundException(RideNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(RideStopNotFoundException.class)
    public ResponseEntity<String> handleRideStopNotFoundException(RideStopNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(InvalidRideStatusException.class)
    public ResponseEntity<String> handleInvalidRideStatusException(InvalidRideStatusException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

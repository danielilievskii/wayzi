package mk.ukim.finki.wayzi.handler;

import mk.ukim.finki.wayzi.exception.LocationNotFoundException;
import mk.ukim.finki.wayzi.exception.VehicleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LocationExceptionHandler {

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<String> handleLocationNotFoundException(LocationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}

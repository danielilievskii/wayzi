package mk.ukim.finki.wayzi.model.exception.base;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
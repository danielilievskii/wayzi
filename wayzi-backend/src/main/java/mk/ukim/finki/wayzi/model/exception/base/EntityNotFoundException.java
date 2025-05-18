package mk.ukim.finki.wayzi.model.exception.base;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message) {
        super(message);
    }
}

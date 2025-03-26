package mk.ukim.finki.wayzi.exception;

public class LocationNotFoundException extends RuntimeException{

    public LocationNotFoundException(Long id) {
        super(String.format("Location with id: %d was not found", id));
    }
}

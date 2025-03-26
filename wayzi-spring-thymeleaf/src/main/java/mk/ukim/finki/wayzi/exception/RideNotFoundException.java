package mk.ukim.finki.wayzi.exception;

public class RideNotFoundException extends RuntimeException{

    public RideNotFoundException(Long id) {
        super(String.format("Ride with id: %d was not found", id));
    }
}

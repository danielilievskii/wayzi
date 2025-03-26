package mk.ukim.finki.wayzi.exception;

public class RideStopNotFoundException extends RuntimeException{

    public RideStopNotFoundException(Long id) {
        super(String.format("Ride stop with id: %d was not found", id));
    }
}

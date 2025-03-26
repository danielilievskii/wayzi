package mk.ukim.finki.wayzi.exception;

public class VehicleNotFoundException extends RuntimeException{

    public VehicleNotFoundException(Long id) {
        super(String.format("Vehicle with id: %d was not found", id));
    }
}

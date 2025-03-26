package mk.ukim.finki.wayzi.exception;

public class AuthenticatedUserNotFoundException extends RuntimeException{

    public AuthenticatedUserNotFoundException() {
        super("Authenticated user was not found.");
    }
}

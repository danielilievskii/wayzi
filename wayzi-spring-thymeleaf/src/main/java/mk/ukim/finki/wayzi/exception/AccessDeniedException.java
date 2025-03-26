package mk.ukim.finki.wayzi.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("Access denied: You don't have the necessary permissions to perform this action.");
    }
}

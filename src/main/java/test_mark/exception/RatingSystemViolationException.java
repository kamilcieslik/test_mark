package test_mark.exception;

public class RatingSystemViolationException extends Exception {
    public RatingSystemViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}

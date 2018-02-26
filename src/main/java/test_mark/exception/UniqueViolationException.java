package test_mark.exception;

public class UniqueViolationException extends Exception {
    public UniqueViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}

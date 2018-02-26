package test_mark.exception;

public class CorrectAnswersViolationException extends Exception {
    public CorrectAnswersViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}

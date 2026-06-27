package vn.edu.hcmuaf.fit.safeguardian.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

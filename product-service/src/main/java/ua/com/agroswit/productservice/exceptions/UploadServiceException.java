package ua.com.agroswit.productservice.exceptions;

public class UploadServiceException extends RuntimeException {
    public UploadServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadServiceException(String message) {
        super(message);
    }
}

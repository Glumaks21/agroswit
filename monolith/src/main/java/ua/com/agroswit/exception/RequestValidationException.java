package ua.com.agroswit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestValidationException extends RuntimeException {

    private final String about;
    private final String description;

    public RequestValidationException(String about, String description) {
        super(about + ": " + description);
        this.about = about;
        this.description = description;
    }
}

package pl.matrasbartosz.zadanieatipera.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.NOT_ACCEPTABLE,
        reason = "Invalid content type for method"
)
public class InvalidContentTypeException extends RuntimeException{
    public InvalidContentTypeException(String message) {
        super(message);
    }
}

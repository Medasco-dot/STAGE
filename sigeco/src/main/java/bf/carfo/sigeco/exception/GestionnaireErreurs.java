package bf.carfo.sigeco.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GestionnaireErreurs {

    @ExceptionHandler(DoublonException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> gererDoublon(DoublonException e) {
        return Map.of("message", e.getMessage());
    }
}
package pokemon.pl.pokemon.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class CoachNotFoundAdvice {

    @ExceptionHandler(CoachNotFoundException.class)
    public String coachNotFoundHandler(CoachNotFoundException ex) {
        return "no-coach";
    }
}

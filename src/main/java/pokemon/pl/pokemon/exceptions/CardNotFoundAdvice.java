package pokemon.pl.pokemon.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CardNotFoundAdvice {

    @ExceptionHandler(CardNotFoundException.class)
    public String cardNotFoundHandler(CardNotFoundException ex) {
        return "no-card";
    }
}


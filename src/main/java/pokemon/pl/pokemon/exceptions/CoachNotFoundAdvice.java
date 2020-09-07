package pokemon.pl.pokemon.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CoachNotFoundAdvice {

    /*@ResponseBody zwraca tylko String albo jakby był obiekt to Jsona*/
    @ExceptionHandler(CoachNotFoundException.class)
   // @ResponseStatus(HttpStatus.NOT_FOUND)
    public String coachNotFoundHandler(CoachNotFoundException ex) {
//        return ex.getMessage();
        return "sukces";
    }
}

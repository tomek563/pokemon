package pokemon.pl.pokemon.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(long id) {
        super("Nie ma tej karty");
    }
}



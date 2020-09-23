package pokemon.pl.pokemon.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException() {
        super("Nie ma tej karty");
    }
}



package pokemon.pl.pokemon.exceptions;

public class CoachNotFoundException extends RuntimeException {
    public CoachNotFoundException(long id) {
        super("Nie ma żadnego trenera");
    }
}

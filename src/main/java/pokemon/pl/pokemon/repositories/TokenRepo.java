package pokemon.pl.pokemon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pokemon.pl.pokemon.model.Token;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {
    Token findByValue(String value);
}

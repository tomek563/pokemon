package pokemon.pl.pokemon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Card;

@Repository
public interface CardRepo extends JpaRepository<Card, Long> {
    Card findByName(String name);
}


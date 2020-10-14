package pokemon.pl.pokemon.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pokemon.pl.pokemon.model.Coach;

@Repository
public interface CoachRepo extends JpaRepository<Coach, Long> {
    Coach findByAppUserId(Long id);

    Coach findByCardsName(String name);
}

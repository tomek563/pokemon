package pokemon.pl.pokemon.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PageService {
    private final CoachService coachService;

    public PageService(CoachService coachService) {
        this.coachService = coachService;
    }

    public Page<Card> buildPagesToShowPokemon(Optional<Integer> page) {
//        page = page < 0 ? 0 : page;
        Coach currentCoach = coachService.findCoachOfLoggedUser();
        List<Card> allCards = currentCoach.getCards();
        Pageable pageable = PageRequest.of(page.orElse(0), 10);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allCards.size());
        return new PageImpl<>(allCards.subList(start, end), pageable, allCards.size());
    }

    public List<Integer> getListOfPageNumbers(Page<Card> pages) {
        int totalPages = pages.getTotalPages();
        if (totalPages > 0) {
            return IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }
//        return null;
        return new ArrayList<>();
    }
}

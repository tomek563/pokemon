package pokemon.pl.pokemon.services;

import org.springframework.stereotype.Service;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.repositories.CardRepo;
import pokemon.pl.pokemon.repositories.CoachRepo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CardService {
    private final CardRepo cardRepo;
    private final CoachRepo coachRepo;
    private final CoachService coachService;
    private static final int DRAW_CARD_COST = 50;

    public CardService(CardRepo cardRepo, CoachRepo coachRepo, CoachService coachService) {
        this.cardRepo = cardRepo;
        this.coachRepo = coachRepo;
        this.coachService = coachService;
    }

    public List<Card> getCardsOnSale() {
        return Stream.of(cardRepo.findAll())
                .flatMap(Collection::stream)
                .filter(Card::isOnSale)
                .collect(Collectors.toList());
    }

    public void setCardOnSaleAndOwner(Card card) {
        Coach coach = coachService.findCoachOfLoggedUser();
        card.setCoach(coach);
        card.setOnSale(true);
        coachRepo.save(coach);
        cardRepo.save(card);
    }

    public List<Card> getFivePokemonCardsAndPayForThem(Coach coach) {
        List<Card> sublistedCards = drawFiveRandomCards();
        sublistedCards.forEach(card -> card.setCoach(coach));
        coach.getCards().addAll(sublistedCards);
        coach.setAmountMoney(coach.getAmountMoney() - DRAW_CARD_COST);
        coachRepo.save(coach);
        return sublistedCards;
    }
    public List<Card> drawFiveRandomCards() {
        List<Card> randomCards = cardRepo.findAll().stream()
                .filter(card -> card.getCoach()==null)
                .collect(Collectors.toList());
        Collections.shuffle(randomCards);
        return randomCards.subList(0, 5);
    }
}

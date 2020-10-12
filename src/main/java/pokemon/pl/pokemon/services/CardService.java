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

    public CardService(CardRepo cardRepo, CoachRepo coachRepo) {
        this.cardRepo = cardRepo;
        this.coachRepo = coachRepo;
    }

    public List<Card> drawFiveRandomCards() {
        List<Card> randomCards = cardRepo.findAll();
        Collections.shuffle(randomCards);
        return randomCards.subList(0, 5);
    }

    public List<Card> getCardsOnSale() {
        return Stream.of(cardRepo.findAll())
                .flatMap(Collection::stream)
                .filter(Card::isOnSale)
                .collect(Collectors.toList());
    }

    public void setCardOnSaleAndOwner(Card card, Coach coach) {
        card.setCoach(coach);
        card.setOnSale(true);
        coachRepo.save(coach);
        cardRepo.save(card);
    }
    public List<Card> getFivePokemonCardsAndPayForThem(Coach coach) {
        List<Card> sublistedCards = drawFiveRandomCards();
        sublistedCards.forEach(card -> card.setCoach(coach));

        coach.getCards().addAll(sublistedCards);
        int drawCardCost = 50;
        coach.setAmountMoney(coach.getAmountMoney() - drawCardCost);
        coachRepo.save(coach);
        return sublistedCards;
    }
}

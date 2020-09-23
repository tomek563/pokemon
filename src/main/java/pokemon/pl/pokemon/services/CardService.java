package pokemon.pl.pokemon.services;

import org.springframework.stereotype.Service;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.repositories.CardRepo;
import pokemon.pl.pokemon.repositories.CoachRepo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CardService {

    private CardRepo cardRepo;
    private CoachRepo coachRepo;

    public CardService(CardRepo cardRepo, CoachRepo coachRepo) {
        this.cardRepo = cardRepo;
        this.coachRepo = coachRepo;
    }

    public List<Card> drawFiveRandomCards() {/*test done*/
        List<Card> randomCards = cardRepo.findAll();
        Collections.shuffle(randomCards);
        return randomCards.subList(0, 5);
    }

    public List<Card> getCardsOnSale() { /*test done*/
        return Stream.of(cardRepo.findAll())
                .flatMap(coaches -> coaches.stream())
                .filter(card -> card.isOnSale())
                .collect(Collectors.toList());
    }


    public void setCardOnSaleAndOwner(Card card, Coach coach) {/*test half done pytanie czy set tez?*/
        card.setCoach(coach);
        card.setOnSale(true);
        coachRepo.save(coach);
        cardRepo.save(card);
    }
}

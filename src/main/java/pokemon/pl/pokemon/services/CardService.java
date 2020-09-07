package pokemon.pl.pokemon.services;

import org.springframework.stereotype.Service;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.repositories.CardRepo;

import java.util.Collections;
import java.util.List;

@Service
public class CardService {

    private CardRepo cardRepo;

    public CardService(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
    }

    public List<Card> drawFiveRandomCards() {
        List<Card> randomCards = cardRepo.findAll();
        Collections.shuffle(randomCards);
        return randomCards.subList(0, 5);
    }
}

package pokemon.pl.pokemon.services;

import org.springframework.stereotype.Service;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Market;
import pokemon.pl.pokemon.repositories.CardRepo;
import pokemon.pl.pokemon.repositories.MarketRepo;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class MarketService {

    private MarketRepo marketRepo;

    public MarketService(MarketRepo marketRepo) {
        this.marketRepo = marketRepo;
    }
    public List<Card> showCardsInMarket() {
        return marketRepo.getOne(1L).getCards();
    }

}

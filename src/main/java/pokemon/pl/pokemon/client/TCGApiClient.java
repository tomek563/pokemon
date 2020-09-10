package pokemon.pl.pokemon.client;
//zapis do bazy sciagnietych kart
//za pomocą pętli zapisz kolene karty z kolejnych stron (13000 kart)

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import pokemon.pl.pokemon.model.Card;
//import pokemon.pl.pokemon.model.Market;
import pokemon.pl.pokemon.repositories.CardRepo;
//import pokemon.pl.pokemon.repositories.MarketRepo;

import javax.annotation.PostConstruct;
import javax.persistence.PostUpdate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class TCGApiClient {
    private CardRepo cardRepo;
    private RestTemplate restTemplate;

    private static final String URL = "https://api.pokemontcg.io/v1/";

    public TCGApiClient(CardRepo cardRepo, RestTemplate restTemplate) {
        this.cardRepo = cardRepo;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void download() {
//        restTemplate.headForHeaders().get()
        // TODO: 02.09.2020 i w zaleznosci od ilosci kart - elasttczne wybieranie kart
        if (cardRepo.findAll().isEmpty()) {
            for (int i = 1; i < 14; i++) {
                final int iCopy = i;
                Thread thread = new Thread(() -> {
                    ArrayList<Card> cards = new ArrayList<>();
                    Cards response = restTemplate.getForObject(URL + "cards?page=" + iCopy + "&pageSize=1000", Cards.class);
                    cards.addAll(response.getCards());
                    cardRepo.saveAll(cards);
                });
                thread.start();
            }
        }

    }


    static class Cards {
        private List<Card> cards;

        public List<Card> getCards() {
            return cards;
        }
    }


}

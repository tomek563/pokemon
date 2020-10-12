package pokemon.pl.pokemon.client;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import pokemon.pl.pokemon.model.Card;
//import pokemon.pl.pokemon.model.Market;
import pokemon.pl.pokemon.repositories.CardRepo;
//import pokemon.pl.pokemon.repositories.MarketRepo;

import javax.annotation.PostConstruct;
import javax.persistence.PostUpdate;
import java.util.*;

@Component
public class TCGApiClient {
    private final CardRepo cardRepo;
    private final RestTemplate restTemplate;
    private static final String URL = "https://api.pokemontcg.io/v1/";
    private static final int pageNumber = 14;

    public TCGApiClient(CardRepo cardRepo, RestTemplate restTemplate) {
        this.cardRepo = cardRepo;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void download() {
        if (cardRepo.count()==0) {
            for (int i = 1; i < pageNumber; i++) {
                createNewThread(i);
            }
        }
    }

    private void createNewThread(int iCopy) {
        Thread thread = new Thread(() -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<Cards> response = restTemplate.exchange(String.format("%scards?page=%d&pageSize=1000",URL,iCopy), HttpMethod.GET,entity, Cards.class);
            List<Card> cards = new ArrayList<>(Objects.requireNonNull(response.getBody()).getCards());
            cardRepo.saveAll(cards);
        });
        thread.start();
    }

    static class Cards {
        private List<Card> cards;
        public List<Card> getCards() {
            return cards;
        }
    }


}

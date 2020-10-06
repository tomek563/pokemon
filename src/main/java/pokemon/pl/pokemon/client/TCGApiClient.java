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
    private CardRepo cardRepo;
    private RestTemplate restTemplate;

    private static final String URL = "https://api.pokemontcg.io/v1/";

    public TCGApiClient(CardRepo cardRepo, RestTemplate restTemplate) {
        this.cardRepo = cardRepo;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void download() {

       // if (cardRepo.findAll().isEmpty()) {
       if(true){
            for (int i = 1; i < 14; i++) {
                final int iCopy = i;
                Thread thread = new Thread(() -> {
                    ArrayList<Card> cards = new ArrayList<>();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
                    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);


//                    Cards response = restTemplate.getForObject(URL + "cards?page=" + iCopy + "&pageSize=1000", Cards.class);
                    ResponseEntity<Cards> response = restTemplate.exchange(URL + "cards?page=" + iCopy + "&pageSize=1000", HttpMethod.GET,entity, Cards.class);
                    System.err.println("Downloaded page: " + iCopy);
                    System.out.println(response.getBody().getCards());
                    cards.addAll(response.getBody().getCards());
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

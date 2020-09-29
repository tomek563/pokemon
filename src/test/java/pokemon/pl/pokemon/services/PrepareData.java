package pokemon.pl.pokemon.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.model.Token;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PrepareData {
    public static AppUser prepareAppUser() {
        return new AppUser(2L, "jannowak@gmail.com");
    }
    public static List<Card> prepareCards() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("1", "Pikachu", 50, false));
        cards.add(new Card("2", "Charmander", 50, true));
        cards.add(new Card("3", "Bulbasaur", 50, true));
        cards.add(new Card("4", "Squirtle", 50, false));
        cards.add(new Card("5", "Wartortle", 50, true));
        cards.add(new Card("6", "Venusaur", 50, false));
        cards.add(new Card("7", "Ivysaur", 50, false));
        return cards;
    }

    public static MimeMessage prepareMimeMessage() throws MessagingException {
        Properties properties = new Properties();
        Session session = Session.getInstance(properties);
        return new MimeMessage(session);
    }
    public static Page<Card> preparePages(List<Card> cards, int page) {
        Pageable pageable = PageRequest.of(page, 4);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), cards.size());
        return new PageImpl<>(cards.subList(start, end), pageable, cards.size());
    }
    public static Token prepareToken() {
        Token token = new Token();
        token.setAppUser(new AppUser(2L, "jannowak@gmail.com"));
        return token;
    }
    public static Coach prepareCoach() {
        Coach coach = new Coach();
        coach.setCoachName("Mark");
        coach.setAmountMoney(50);
        coach.setCards(prepareCards());
        return coach;
    }
    public static Card prepareCard() {
        return new Card("4", "Squirtle", 50, true);
    }
}

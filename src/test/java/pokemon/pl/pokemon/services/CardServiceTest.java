package pokemon.pl.pokemon.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.repositories.CardRepo;
import pokemon.pl.pokemon.repositories.CoachRepo;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {

    @Mock
    CardRepo cardRepo;
    @Mock
    CoachRepo coachRepo;

    @InjectMocks
    CardService cardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void drawFiveRandomCards() {
//        given
        when(cardRepo.findAll()).thenReturn(prepareCards());
//        when
//        then
        List<Card> returnedCards = cardService.drawFiveRandomCards();
        assertThat(returnedCards, hasSize(5));
        assertTrue(prepareCards().containsAll(returnedCards));
    }

    @Test
    void getCardsOnSale() {
//        given
        when(cardRepo.findAll()).thenReturn(prepareCards());
//        when
//        then
        assertThat(cardService.getCardsOnSale().size(), equalTo(3));
        assertThat(cardService.getCardsOnSale().get(0).getName(), equalTo("Charmander"));
    }

    @Test
    void setCardOnSaleAndOwner() {
//        given
        Card card = new Card();
        Coach coach = new Coach();
        cardService.setCardOnSaleAndOwner(card, coach);
//        when
//        then
        assertThat(card.getCoach(), equalTo(coach));
        assertThat(card.isOnSale(), equalTo(true));
        verify(coachRepo).save(coach);
        verify(cardRepo).save(card);
    }

    private List<Card> prepareCards() {
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

}
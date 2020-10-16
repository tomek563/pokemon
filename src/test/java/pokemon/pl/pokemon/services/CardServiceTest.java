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
    @Mock
    CoachService coachService;
    @InjectMocks
    CardService cardService;
    List<Card> cards;
    Card card;
    Coach coach;
    int drawCardCost = 50;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cards = PrepareData.prepareCards();
        card = PrepareData.prepareCard();
        coach = PrepareData.prepareCoach();
    }

    @Test
    void drawFiveRandomCards_Should_Return_ListOf_Exact_FiveCards() {
//        given
        when(cardRepo.findAll()).thenReturn(cards);
//        when
//        then
        List<Card> returnedCards = cardService.drawFiveRandomCards();
        assertThat(returnedCards, hasSize(5));
        assertTrue(cards.containsAll(returnedCards));
    }

    @Test
    void getCardsOnSale_Should_Get_Only_Cards_OnSale() {
//        given
        when(cardRepo.findAll()).thenReturn(cards);
//        when
//        then
        assertThat(cardService.getCardsOnSale().size(), equalTo(3));
        assertThat(cardService.getCardsOnSale().get(0).getName(), equalTo("Charmander"));
    }

    @Test
    void setCardOnSaleAndOwner_Should_Set_Card_Properties() {
//        given
        when(coachService.findCoachOfLoggedUser()).thenReturn(coach);
        cardService.setCardOnSaleAndOwner(card);
//        when
//        then
        assertThat(card.getCoach(), equalTo(coach));
        assertThat(card.isOnSale(), equalTo(true));
        verify(coachRepo).save(coach);
        verify(cardRepo).save(card);
    }

    @Test
    void getFivePokemonCardsAndPayForThem_Should_Reduce_Coach_Money_By_CardCost_And_Give_Him_FiveCards() {
        int moneyBeforeTest = coach.getAmountMoney();
        coach.getCards().clear();

        List<Card> preparedFiveCards = cards.subList(0, 5);
        when(cardRepo.findAll()).thenReturn(preparedFiveCards);
        CardService cardService = new CardService(cardRepo, coachRepo, null);
        cardService.getFivePokemonCardsAndPayForThem(coach);

        for (Card card : preparedFiveCards) {
            assertThat(card.getCoach(), equalTo(coach));
        }
        assertThat(coach.getAmountMoney(), equalTo(moneyBeforeTest - drawCardCost));
        assertThat(coach.getCards().size(), equalTo(preparedFiveCards.size()));
        verify(coachRepo).save(coach);
    }

}
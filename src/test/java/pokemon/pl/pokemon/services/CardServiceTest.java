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
    List<Card> cards;
    Card card;
    Coach coach;

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
        cardService.setCardOnSaleAndOwner(card, coach);
//        when
//        then
        assertThat(card.getCoach(), equalTo(coach));
        assertThat(card.isOnSale(), equalTo(true));
        verify(coachRepo).save(coach);
        verify(cardRepo).save(card);
    }

}
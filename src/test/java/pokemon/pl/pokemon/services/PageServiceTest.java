package pokemon.pl.pokemon.services;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

class PageServiceTest {

    @Mock
    CoachService coachService;

    @InjectMocks
    PageService pageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void buildPagesToShowPokemon() {
//        given
        Optional<Integer> page = Optional.of(0);
        when(coachService.findCoachOfLoggedUser()).thenReturn(prepareCoach());
        Page<Card> cards = pageService.buildPagesToShowPokemon(page);
//        then
        assertThat(cards.getTotalPages(), equalTo(1));
        assertThat(cards.get().count(), equalTo(7L));
    }

    @Test
    void getListOfPageNumbers_Should_Return_List_Of_Integers_When_TotalPages_Not_Equal_Zero() {
//        given
        Page<Card> pages = preparePages(prepareCards(), 1);
        int totalPages = pages.getTotalPages();
//        when
        List<Integer> listOfPageNumbers = pageService.getListOfPageNumbers(pages);
//        then
        assertThat(totalPages, greaterThan(0));
        assertThat(listOfPageNumbers.size(), equalTo(totalPages));
    }

    @Test
    void getListOfPageNumbers_Should_Return_EmptyList_When_TotalPages_Equal_Zero() {
//        given
        List<Card> preparedCards = prepareCards();
        preparedCards.clear();
        Page<Card> pages = preparePages(preparedCards, 0);
        List<Integer> listOfPageNumbers = pageService.getListOfPageNumbers(pages);
//        then
        assertThat(pages.getTotalPages(), equalTo(0));
        assertThat(listOfPageNumbers.isEmpty(), equalTo(true));
    }

    private Page<Card> preparePages(List<Card> cards, int page) {
        Pageable pageable = PageRequest.of(page, 4);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), cards.size());
        return new PageImpl<>(cards.subList(start, end), pageable, cards.size());
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

    private Coach prepareCoach() {
        Coach coach = new Coach();
        coach.setCoachName("Mark");
        coach.setAmountMoney(50);
        coach.setCards(prepareCards());
        return coach;
    }
}
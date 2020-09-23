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
    Coach coach;
    List<Card> cards;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        coach = PrepareData.prepareCoach();
        cards = PrepareData.prepareCards();
    }

    @Test
    void buildPagesToShowPokemon_Should_Return_Proper_Pages() {
//        given
        Optional<Integer> page = Optional.of(0);
        when(coachService.findCoachOfLoggedUser()).thenReturn(coach);
        Page<Card> cards = pageService.buildPagesToShowPokemon(page);
//        then
        assertThat(cards.getTotalPages(), equalTo(1));
        assertThat(cards.get().count(), equalTo(7L));
    }

    @Test
    void getListOfPageNumbers_Should_Return_List_Of_Integers_When_TotalPages_Not_Equal_Zero() {
//        given
        Page<Card> pages = PrepareData.preparePages(cards, 1);
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
        cards.clear();
        Page<Card> pages = PrepareData.preparePages(cards, 0);
        List<Integer> listOfPageNumbers = pageService.getListOfPageNumbers(pages);
//        then
        assertThat(pages.getTotalPages(), equalTo(0));
        assertThat(listOfPageNumbers.isEmpty(), equalTo(true));
    }

}
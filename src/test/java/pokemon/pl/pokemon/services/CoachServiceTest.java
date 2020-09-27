package pokemon.pl.pokemon.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pokemon.pl.pokemon.exceptions.CardNotFoundException;
import pokemon.pl.pokemon.exceptions.CoachNotFoundException;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.repositories.CardRepo;
import pokemon.pl.pokemon.repositories.CoachRepo;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

class CoachServiceTest {
    @Mock
    CoachRepo coachRepo;
    @Mock
    AppUserService appUserService;
    @Mock
    CardRepo cardRepo;
    @Mock
    CardService cardService;
    int drawCardCost = 50;
    Card card;
    Coach coachFirst;
    Coach coachSecond;
    AppUser appUser;
    List<Card> cards;
    String nameFirst = "Squirtle";
    String nameSecond = "Seal";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        card = PrepareData.prepareCard();
        coachFirst = PrepareData.prepareCoach();
        coachSecond = PrepareData.prepareCoach();
        appUser = PrepareData.prepareAppUser();
        cards = PrepareData.prepareCards();
    }

    @Test
    void isCoachRepoEmpty_Should_ReturnTrue_forEmptyRepo() {
//        given
        Long emptyRepo = 0L;
        when(coachRepo.count()).thenReturn(emptyRepo);
        CoachService coachService = new CoachService(coachRepo, null, null, null);
//        when
//        then
        assertThat(coachService.isCoachRepoEmpty(), is(true));
    }

    @Test
    void isCoachRepoEmpty_Should_ReturnFalse_forNotEmptyRepo() {
//        given
        Long notEmptyRepo = 1L;
        when(coachRepo.count()).thenReturn(notEmptyRepo);
        CoachService coachService = new CoachService(coachRepo, null, null, null);
//        when
//        then
        assertThat(coachService.isCoachRepoEmpty(), is(false));
    }

    @Test
    void findByCardsName_Should_Ask_Repo_For_CardOfGivenName() { // refactor na przekazywanie name
//        given
        CoachService coachService = new CoachService(coachRepo, null, null, null);
        coachService.findByCardsName(card);
//        then
        verify(coachRepo, times(1)).findByCardsName(card.getName());
    }

    @Test
    void hasCoachMoneyToDrawCard_Should_ReturnTrue_When_Coach_Has_Enough_Money() {
//        given
        CoachService coachService = new CoachService(null, null, null, null);
//        then
        assertThat(coachService.hasCoachHasMoneyToDrawCard(coachFirst), equalTo(true));
    }

    @Test
    void hasCoachMoneyToDrawCard_Should_ReturnFalse_When_Coach_Has_Not_Sufficient_Money() {
//        given
        coachFirst.setAmountMoney(40);
        CoachService coachService = new CoachService(null, null, null, null);
//        then
        assertThat(coachService.hasCoachHasMoneyToDrawCard(coachFirst), equalTo(false));
    }

    @Test
    void getCardOfCoachWith_Should_ReturnProperCard_When_Coach_HasGot_That_Card() {
//        given
        CoachService coachService = new CoachService(null, null, null, null);
//        then
        assertThat(coachService.getCardOfCoachWith(nameFirst, coachFirst), equalTo(card));
    }

    @Test
    void getCardOfCoachWith_Should_ThrowCardNotFoundException_When_Coach_HasNotGot_That_Card() {
//        given
        CoachService coachService = new CoachService(null, null, null, null);
//        then
        assertThrows(CardNotFoundException.class, () -> coachService.getCardOfCoachWith(nameSecond, coachFirst));
    }

    @Test
    void hasCoachEnoughMoneyToBuyCard_Should_ReturnTrue_When_He_HasGot() {
//        given
        CoachService coachService = new CoachService(null, null, null, null);
//        then
        assertThat(coachService.hasCoachEnoughMoneyToBuyCard(coachFirst, card), equalTo(true));
    }

    @Test
    void hasCoachEnoughMoneyToBuyCard_Should_ReturnFalse_When_He_HasNot() {
//        given
        coachFirst.setAmountMoney(40);
        CoachService coachService = new CoachService(null, null, null, null);
//        then
        assertThat(coachService.hasCoachEnoughMoneyToBuyCard(coachFirst, card), equalTo(false));
    }

    @Test
    void addMoneyAfterOneHour_Should_AskCoachRepo_OneTime() {
//        given
        CoachService coachService = new CoachService(coachRepo, null, null, null);
        coachService.addMoneyAfterOneHour();
//        then
        verify(coachRepo, times(1)).findAll();
    }

    @Test
    void addMoneyAfterOneHour_Should_Add_Every_Coach_Extra_Money() {
//        given
        int coachFirstMoneyBeforeTest = coachFirst.getAmountMoney();
        int coachSecondMoneyBeforeTest = coachSecond.getAmountMoney();
        List<Coach> coaches = new ArrayList<>();
        coaches.add(coachFirst);
        coaches.add(coachSecond);

        when(coachRepo.findAll()).thenReturn(coaches);
        CoachService coachService = new CoachService(coachRepo, null, null, null);
        coachService.addMoneyAfterOneHour();
//        then
        assertThat(coaches.get(0).getAmountMoney(), equalTo(coachFirstMoneyBeforeTest + 100));
        assertThat(coaches.get(1).getAmountMoney(), equalTo(coachSecondMoneyBeforeTest + 100));
    }

    @Test
    void getLoggedUserId_Should_Return_LoggedUserId() {
//        given
        when(appUserService.getCurrentUser()).thenReturn(appUser);
        CoachService coachService = new CoachService(null, null, null, appUserService);
//        then
        assertThat(coachService.getLoggedUserId(), equalTo(2L));
    }

    @Test
    void hasUserGotCoach_Should_Return_False_If_There_Is_No_Coach() {
//        given
        when(coachRepo.findByAppUserId(anyLong())).thenReturn(null);
        when(appUserService.getCurrentUser()).thenReturn(appUser);
        CoachService coachService = new CoachService(coachRepo, null, null, appUserService);
//        then
        assertThat(coachService.hasUserGotCoach(), equalTo(false));
    }

    @Test
    void hasUserGotCoach_Should_Return_True_If_There_Is_Coach() {
//        given
        when(coachRepo.findByAppUserId(anyLong())).thenReturn(coachFirst);
        when(appUserService.getCurrentUser()).thenReturn(appUser);
        CoachService coachService = new CoachService(coachRepo, null, null, appUserService);
//        then
        assertThat(coachService.hasUserGotCoach(), equalTo(true));
    }

    @Test
    void addCoach_Should_Ask_CoachRepo_OneTime_And_Set_User_ToCoach() {
//        given
        when(appUserService.getCurrentUser()).thenReturn(appUser);
        CoachService coachService = new CoachService(coachRepo, null, null, appUserService);
        coachService.addCoach(coachFirst);
//        then
        assertThat(coachFirst.getAppUser(), equalTo(appUser));
        verify(coachRepo).save(coachFirst);
    }

    @Test
    void findCoachOfLoggedUserShouldReturnCoach() {
//        given
        when(coachRepo.findByAppUserId(anyLong())).thenReturn(coachFirst);
        when(appUserService.getCurrentUser()).thenReturn(appUser);
        CoachService coachService = new CoachService(coachRepo, null, null, appUserService);
//        then
        assertThat(coachService.findCoachOfLoggedUser(), equalTo(coachFirst));
    }

    @Test
    void findCoachOfLoggedUser_Should_Throw_CoachNotFoundException_If_Coach_Does_Not_Exist() {
//        given
        when(coachRepo.findByAppUserId(anyLong())).thenReturn(null);
        when(appUserService.getCurrentUser()).thenReturn(appUser);
        CoachService coachService = new CoachService(coachRepo, null, null, appUserService);
//        then
        assertThrows(CoachNotFoundException.class, () -> coachService.findCoachOfLoggedUser());
    }

    @Test
    void finishTransaction_Should_Ask_CoachRepo_TwoTimes() {
//        given
        CoachService coachService = new CoachService(coachRepo, cardRepo, null, null);
        coachService.finishTransaction(coachFirst, coachSecond, card);
//        then
        verify(coachRepo, times(2)).save(any());
    }

    @Test
    void finishTransaction_Should_Ask_CardRepo_OneTime() {
//        given
        CoachService coachService = new CoachService(coachRepo, cardRepo, null, null);
        coachService.finishTransaction(coachFirst, coachSecond, card);
//        then
        verify(cardRepo).save(any());
    }

    @Test
    void finishTransaction_Should_Change_Card_Properties() {
//        given
        boolean cardNotOnSaleBeforeTest = card.isOnSale();
        Coach noCoachBeforeTest = card.getCoach();

        CoachService coachService = new CoachService(coachRepo, cardRepo, null, null);
        coachService.finishTransaction(coachFirst, coachSecond, card);

//        then
        assertThat(card.isOnSale(), equalTo(!cardNotOnSaleBeforeTest));
        assertThat(card.getCoach(), not(equalTo(noCoachBeforeTest)));
    }

    @Test
    void finishTransaction_ShouldHaveBeenGiven_CurrentOwnerOfTheCard_Money_For_Card() {
//        given
        int coachFirstMoneyBeforeTest = coachFirst.getAmountMoney();

        CoachService coachService = new CoachService(coachRepo, cardRepo, null, null);
        coachService.finishTransaction(coachFirst, coachSecond, card);
//        then
        assertThat(coachFirst.getAmountMoney(), equalTo(coachFirstMoneyBeforeTest + card.getPrice()));
    }

    @Test
    void finishTransaction_Should_Add_One_Card_And_Decrease_AmountOfMoney_Of_CoachOfLoggedUserProperties() {
//        given
        int coachAmountMoneyBeforeTest = coachSecond.getAmountMoney();
        int coachAmountOfCardsBeforeTest = coachSecond.getCards().size();
        CoachService coachService = new CoachService(coachRepo, cardRepo, null, null);
        coachService.finishTransaction(coachFirst, coachSecond, card);
//        then
        assertThat(coachSecond.getAmountMoney(), equalTo(coachAmountMoneyBeforeTest - card.getPrice()));
        assertThat(coachSecond.getCards().size(), equalTo(coachAmountOfCardsBeforeTest + 1));
    }

    @Test
    void getFivePokemonCardsAndPayForThem_Should_Reduce_Coach_Money_By_CardCost_And_Give_Him_FiveCards() {
        int moneyBeforeTest = coachFirst.getAmountMoney();
        coachFirst.getCards().clear();

        List<Card> preparedFiveCards = cards.subList(0, 5);
        when(cardService.drawFiveRandomCards()).thenReturn(preparedFiveCards);
        CoachService coachService = new CoachService(coachRepo, null, cardService, null);
        coachService.getFivePokemonCardsAndPayForThem(coachFirst);

        for (Card card : preparedFiveCards) {
            assertThat(card.getCoach(), equalTo(coachFirst));
        }
        assertThat(coachFirst.getAmountMoney(), equalTo(moneyBeforeTest - drawCardCost));
        assertThat(coachFirst.getCards().size(), equalTo(preparedFiveCards.size()));
        verify(coachRepo).save(coachFirst);
    }
}
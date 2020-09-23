package pokemon.pl.pokemon.services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    private int drawCardCost = 50;

    @Test
    void isCoachRepoEmpty_Should_ReturnTrue_forEmptyRepo() {
        CoachRepo coachRepo = mock(CoachRepo.class);
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
        CoachRepo coachRepo = mock(CoachRepo.class);
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
        Card card = prepareCard();
        CoachRepo coachRepo = mock(CoachRepo.class);
        CoachService coachService = new CoachService(coachRepo, null, null, null);
        coachService.findByCardsName(card);
//        then
        verify(coachRepo, times(1)).findByCardsName(card.getName());
    }

    @Test
    void hasCoachMoneyToDrawCard_Should_ReturnTrue_When_Coach_Has_Enough_Money() {
//        given
        Coach coach = prepareCoach();
        CoachService coachService = new CoachService(null, null, null, null);
//        then
        assertThat(coachService.hasCoachHasMoneyToDrawCard(coach), equalTo(true));
    }

    @Test
    void hasCoachMoneyToDrawCard_Should_ReturnFalse_When_Coach_Has_Not_Sufficient_Money() {
//        given
        Coach coach = prepareCoach();
        coach.setAmountMoney(40);
        CoachService coachService = new CoachService(null, null, null, null);
//        then
        assertThat(coachService.hasCoachHasMoneyToDrawCard(coach), equalTo(false));
    }

    @Test
    void getCardOfCoachWith_Should_ReturnProperCard_When_Coach_HasGot_That_Card() {
//        given
        String name = "Squirtle";
        Coach coach = prepareCoach();
        Card card = prepareCard();
        CoachService coachService = new CoachService(null, null, null, null);

//        then
        assertThat(coachService.getCardOfCoachWith(name, coach), equalTo(card));
    }

    @Test
    void getCardOfCoachWith_Should_ThrowCardNotFoundException_When_Coach_HasNotGot_That_Card() {
//        given
        String name = "Seal";
        Coach coach = prepareCoach();
        CoachService coachService = new CoachService(null, null, null, null);
//        then
        assertThrows(CardNotFoundException.class, () -> coachService.getCardOfCoachWith(name, coach));
    }

    @Test
    void hasCoachEnoughMoneyToBuyCard_Should_ReturnTrue_When_He_HasGot() {
//        given
        Coach coach = prepareCoach();
        Card card = prepareCard();
        CoachService coachService = new CoachService(null, null, null, null);

//        then
        assertThat(coachService.hasCoachEnoughMoneyToBuyCard(coach, card), equalTo(true));
    }
    @Test
    void hasCoachEnoughMoneyToBuyCard_Should_ReturnFalse_When_He_HasNot() {
//        given
        Coach coach = prepareCoach();
        coach.setAmountMoney(40);
        Card card = prepareCard();
        CoachService coachService = new CoachService(null, null, null, null);
//        then
        assertThat(coachService.hasCoachEnoughMoneyToBuyCard(coach, card), equalTo(false));
    }

    @Test
    void addMoneyAfterOneHour_Should_AskCoachRepo_OneTime() {
//        given
        CoachRepo coachRepo = mock(CoachRepo.class);
        CoachService coachService = new CoachService(coachRepo, null, null, null);
        coachService.addMoneyAfterOneHour();
//        then
        verify(coachRepo, times(1)).findAll();
    }
    @Test
    void addMoneyAfterOneHour_Should_Add_Every_Coach_Extra_Money() {
//        given
        Coach coach = prepareCoach();
        Coach coach2 = prepareCoach();
        int coachMoneyBeforeTest = coach.getAmountMoney();
        int coach2MoneyBeforeTest = coach2.getAmountMoney();
        List<Coach> coaches = new ArrayList<>();
        coaches.add(coach);
        coaches.add(coach2);

        CoachRepo coachRepo = mock(CoachRepo.class);
        when(coachRepo.findAll()).thenReturn(coaches);
        CoachService coachService = new CoachService(coachRepo, null, null, null);
        coachService.addMoneyAfterOneHour();
//        then
        assertThat(coaches.get(0).getAmountMoney(), equalTo(coachMoneyBeforeTest+100));
        assertThat(coaches.get(1).getAmountMoney(), equalTo(coach2MoneyBeforeTest+100));
    }

    @Test
    void getLoggedUserId_Should_Return_LoggedUserId() {
//        given
        AppUserService appUserService = mock(AppUserService.class);
        when(appUserService.getCurrentUser()).thenReturn(prepareAppUser());
        CoachService coachService = new CoachService(null, null, null, appUserService);
//        then
        assertThat(coachService.getLoggedUserId(), equalTo(2L));
    }

    @Test
    void hasUserGotCoach_Should_Return_False_If_There_Is_No_Coach() {
//        given
        CoachRepo coachRepo = mock(CoachRepo.class);
        AppUserService appUserService = mock(AppUserService.class);
        when(coachRepo.findByAppUserId(anyLong())).thenReturn(null);
        when(appUserService.getCurrentUser()).thenReturn(prepareAppUser());
        CoachService coachService = new CoachService(coachRepo, null, null, appUserService);
//        then
        assertThat(coachService.hasUserGotCoach(), equalTo(false));
    }

    @Test
    void hasUserGotCoach_Should_Return_True_If_There_Is_Coach() {
//        given
        Coach coach = prepareCoach();
        CoachRepo coachRepo = mock(CoachRepo.class);
        when(coachRepo.findByAppUserId(anyLong())).thenReturn(coach);
        AppUserService appUserService = mock(AppUserService.class);
        when(appUserService.getCurrentUser()).thenReturn(prepareAppUser());
        CoachService coachService = new CoachService(coachRepo, null, null, appUserService);
//        then
        assertThat(coachService.hasUserGotCoach(), equalTo(true));
    }

    @Test
    void addCoach_Should_Ask_CoachRepo_OneTime_And_Set_User_ToCoach() {
//        given
        Coach coach = prepareCoach();
        CoachRepo coachRepo = mock(CoachRepo.class);
        AppUserService appUserService = mock(AppUserService.class);
        when(appUserService.getCurrentUser()).thenReturn(prepareAppUser());
        CoachService coachService = new CoachService(coachRepo, null, null, appUserService);
        coachService.addCoach(coach);
//        then
        assertThat(coach.getAppUser(), equalTo(prepareAppUser()));
        verify(coachRepo).save(coach);
    }

    @Test
    void findCoachOfLoggedUserShouldReturnCoach() {
//        given
        CoachRepo coachRepo = mock(CoachRepo.class);
        when(coachRepo.findByAppUserId(anyLong())).thenReturn(prepareCoach());
        AppUserService appUserService = mock(AppUserService.class);
        when(appUserService.getCurrentUser()).thenReturn(prepareAppUser());
        CoachService coachService = new CoachService(coachRepo, null, null, appUserService);
//        then
        assertThat(coachService.findCoachOfLoggedUser(), equalTo(prepareCoach()));
    }

    @Test
    void findCoachOfLoggedUser_Should_Throw_CoachNotFoundException_If_Coach_Does_Not_Exist() {
//        given
        CoachRepo coachRepo = mock(CoachRepo.class);
        when(coachRepo.findByAppUserId(anyLong())).thenReturn(null);
        AppUserService appUserService = mock(AppUserService.class);
        when(appUserService.getCurrentUser()).thenReturn(prepareAppUser());
        CoachService coachService = new CoachService(coachRepo, null, null, appUserService);
//        then
        assertThrows(CoachNotFoundException.class, () -> coachService.findCoachOfLoggedUser());
    }

    @Test
    void finishTransaction_Should_Ask_CoachRepo_TwoTimes() {
//        given
        Coach currentOwnerOfTheCard = prepareCoach();
        Coach coachOfLoggedUser = prepareCoach();
        Card card = prepareCard();
        CoachRepo coachRepo = mock(CoachRepo.class);
        CardRepo cardRepo = mock(CardRepo.class);
        CoachService coachService = new CoachService(coachRepo, cardRepo, null, null);
        coachService.finishTransaction(currentOwnerOfTheCard, coachOfLoggedUser, card);
//        then
        verify(coachRepo, times(2)).save(any());
    }

    @Test
    void finishTransaction_Should_Ask_CardRepo_OneTime() {
//        given
        Coach currentOwnerOfTheCard = prepareCoach();
        Coach coachOfLoggedUser = prepareCoach();
        Card card = prepareCard();
        CoachRepo coachRepo = mock(CoachRepo.class);
        CardRepo cardRepo = mock(CardRepo.class);
        CoachService coachService = new CoachService(coachRepo, cardRepo, null, null);
        coachService.finishTransaction(currentOwnerOfTheCard, coachOfLoggedUser, card);
//        then
        verify(cardRepo).save(any());
    }

    @Test
    void finishTransaction_Should_Change_Card_Properties() {
//        given
        Coach currentOwnerOfTheCard = prepareCoach();
        Coach coachOfLoggedUser = prepareCoach();
        Card card = prepareCard();
        boolean cardNotOnSaleBeforeTest = card.isOnSale();
        Coach noCoachBeforeTest = card.getCoach();

        CoachRepo coachRepo = mock(CoachRepo.class);
        CardRepo cardRepo = mock(CardRepo.class);

        CoachService coachService = new CoachService(coachRepo, cardRepo, null, null);
        coachService.finishTransaction(currentOwnerOfTheCard, coachOfLoggedUser, card);

//        then
        assertThat(card.isOnSale(), equalTo(!cardNotOnSaleBeforeTest));
        assertThat(card.getCoach(), not(equalTo(noCoachBeforeTest)));
    }

    @Test
    void finishTransaction_ShouldHaveBeenGiven_CurrentOwnerOfTheCard_Money_For_Card() {
//        given
        Coach currentOwnerOfTheCard = prepareCoach();
        Coach coachOfLoggedUser = prepareCoach();
        Card card = prepareCard();

        int currentOwnerAmountMoneyBeforeTest = currentOwnerOfTheCard.getAmountMoney();

        CoachRepo coachRepo = mock(CoachRepo.class);
        CardRepo cardRepo = mock(CardRepo.class);

        CoachService coachService = new CoachService(coachRepo, cardRepo, null, null);
        coachService.finishTransaction(currentOwnerOfTheCard, coachOfLoggedUser, card);

//        then
        assertThat(currentOwnerOfTheCard.getAmountMoney(), equalTo(currentOwnerAmountMoneyBeforeTest + card.getPrice()));
    }

    @Test
    void finishTransaction_Should_Add_One_Card_And_Decrease_AmountOfMoney_Of_CoachOfLoggedUserProperties() {
//        given
        Coach currentOwnerOfTheCard = prepareCoach();
        Coach coachOfLoggedUser = prepareCoach();
        Card card = prepareCard();
        int coachOfLoggedUserAmountMoneyBeforeTest = coachOfLoggedUser.getAmountMoney();
        int coachOfLoggedUserAmountOfCardsBeforeTest = coachOfLoggedUser.getCards().size();
        CoachRepo coachRepo = mock(CoachRepo.class);
        CardRepo cardRepo = mock(CardRepo.class);
        CoachService coachService = new CoachService(coachRepo, cardRepo, null, null);
        coachService.finishTransaction(currentOwnerOfTheCard, coachOfLoggedUser, card);
//        then
        assertThat(coachOfLoggedUser.getAmountMoney(), equalTo(coachOfLoggedUserAmountMoneyBeforeTest - card.getPrice()));
        assertThat(coachOfLoggedUser.getCards().size(), equalTo(coachOfLoggedUserAmountOfCardsBeforeTest + 1));
    }

    @Test
    void getFivePokemonCardsAndPayForThem_Should_Reduce_Coach_Money_By_CardCost_And_Give_Him_FiveCards() {
        Coach coach = prepareCoach();
        int moneyBeforeTest = coach.getAmountMoney();
        coach.getCards().clear();

        CardService cardService = mock(CardService.class);
        CoachRepo coachRepo = mock(CoachRepo.class);

        List<Card> preparedFiveCards = prepareCards().subList(0, 5);
        when(cardService.drawFiveRandomCards()).thenReturn(preparedFiveCards);
        CoachService coachService = new CoachService(coachRepo, null, cardService, null);
        coachService.getFivePokemonCardsAndPayForThem(coach);

        for (Card card : preparedFiveCards) {
            assertThat(card.getCoach(),equalTo(coach));
        }
        assertThat(coach.getAmountMoney(),equalTo(moneyBeforeTest-drawCardCost));
        assertThat(coach.getCards().size(),equalTo(preparedFiveCards.size()));
        verify(coachRepo).save(coach);
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

    private Card prepareCard() {
        return new Card("4", "Squirtle", 50, true);
    }

    private Coach prepareCoach() {
        Coach coach = new Coach();
        coach.setCoachName("Mark");
        coach.setAmountMoney(50);
        coach.setCards(prepareCards());
        return coach;
    }
    private AppUser prepareAppUser() {
        return new AppUser(2L, "jannowak@gmail.com");
    }
}

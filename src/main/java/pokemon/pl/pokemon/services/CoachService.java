package pokemon.pl.pokemon.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pokemon.pl.pokemon.exceptions.CardNotFoundException;
import pokemon.pl.pokemon.exceptions.CoachNotFoundException;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.repositories.CardRepo;
import pokemon.pl.pokemon.repositories.CoachRepo;

import java.util.Optional;

@Service
public class CoachService {
    private final CoachRepo coachRepo;
    private final CardRepo cardRepo;
    private final AppUserService appUserService;
    private final CurrentUserProvider currentUserProvider;
    private static final int EXTRA_MONEY = 100;
    private static final int DRAW_CARD_COST = 50;

    public CoachService(CoachRepo coachRepo, CardRepo cardRepo, AppUserService appUserService, CurrentUserProvider currentUserProvider) {
        this.coachRepo = coachRepo;
        this.cardRepo = cardRepo;
        this.appUserService = appUserService;
        this.currentUserProvider = currentUserProvider;
    }

    public void addCoach(Coach coach) {
        AppUser currentUser = currentUserProvider.getCurrentUser();
        coach.setAppUser(currentUser);
        coachRepo.save(coach);
    }

    public Coach findCoachOfLoggedUser() {
        Long principalId = appUserService.getLoggedUserId();
        Optional<Coach> byAppUserId = Optional.ofNullable(coachRepo.findByAppUserId(principalId));
        return byAppUserId.orElseThrow(() -> new CoachNotFoundException(1L));
    }

    public boolean hasUserGotCoach() {
        Long principalId = appUserService.getLoggedUserId();
        return coachRepo.findByAppUserId(principalId) != null;
    }

    Coach findByCardsName(Card card) { /*test done*/
        return coachRepo.findByCardsName(card.getName());
    }

    @Scheduled(fixedRate = 3600000, initialDelay = 3600000)
    void addMoneyAfterSomeTime() {
        coachRepo.findAll()
                .forEach(coach -> {
                    coach.setAmountMoney(coach.getAmountMoney() + EXTRA_MONEY);
                    coachRepo.save(coach);
                });
    }

    public boolean hasCoachEnoughMoneyToBuyCard(Card card) {
        Coach currentCoach = findCoachOfLoggedUser();
        return currentCoach.getAmountMoney() >= card.getPrice();
    }

    public void finishTransaction(Card card) {
        Coach currentOwnerOfTheCard = findByCardsName(card);
        Coach currentCoach = findCoachOfLoggedUser();

        card.setOnSale(false);
        card.setCoach(currentCoach);
        currentCoach.getCards().add(card);
        currentCoach.setAmountMoney(currentCoach.getAmountMoney() - card.getPrice());
        currentOwnerOfTheCard.setAmountMoney(currentOwnerOfTheCard.getAmountMoney() + card.getPrice());
        cardRepo.save(card);
        coachRepo.save(currentCoach);
        coachRepo.save(currentOwnerOfTheCard);
    }

    public boolean hasCoachHasMoneyToDrawCard(Coach coach) {
        return coach.getAmountMoney() >= DRAW_CARD_COST;
    }

    public Card getCardOfCurrentCoachWithCard(String name) {
        return findCoachOfLoggedUser().getCards().stream()
                .filter(card -> card.getName().equals(name))
                .findFirst()
                .orElseThrow(CardNotFoundException::new);
    }

    boolean isCoachRepoEmpty() {
        return coachRepo.count() == 0;
    }
}

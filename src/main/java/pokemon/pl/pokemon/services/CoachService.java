package pokemon.pl.pokemon.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pokemon.pl.pokemon.exceptions.CardNotFoundException;
import pokemon.pl.pokemon.exceptions.CoachNotFoundException;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.repositories.CardRepo;
import pokemon.pl.pokemon.repositories.CoachRepo;

import java.util.List;
import java.util.Optional;
import java.util.Timer;

@Service
public class CoachService {
    private final CoachRepo coachRepo;
    private final CardRepo cardRepo;
    private final AppUserService appUserService;
    private final int extraMoney = 100;

    public CoachService(CoachRepo coachRepo, CardRepo cardRepo, AppUserService appUserService) {
        this.coachRepo = coachRepo;
        this.cardRepo = cardRepo;
        this.appUserService = appUserService;
    }

    public void addCoach(Coach coach) {
        AppUser currentUser = appUserService.getCurrentUser();
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

    public Coach findByCardsName(Card card) { /*test done*/
        return coachRepo.findByCardsName(card.getName());
    }

    @Scheduled(fixedRate = 300000, initialDelay = 300000)
    public void addMoneyAfterSomeTime() {
        coachRepo.findAll()
                .forEach(coach -> {
                    coach.setAmountMoney(coach.getAmountMoney() + extraMoney);
                    coachRepo.save(coach);
                });
    }

    public boolean hasCoachEnoughMoneyToBuyCard(Coach coach, Card card) {
        return coach.getAmountMoney() >= card.getPrice();
    }

    public void finishTransaction(Coach currentOwnerOfTheCard, Coach coachOfLoggedUser, Card card) {
        card.setOnSale(false);
        card.setCoach(coachOfLoggedUser);
        coachOfLoggedUser.getCards().add(card);
        coachOfLoggedUser.setAmountMoney(coachOfLoggedUser.getAmountMoney() - card.getPrice());
        currentOwnerOfTheCard.setAmountMoney(currentOwnerOfTheCard.getAmountMoney() + card.getPrice());
        cardRepo.save(card);
        coachRepo.save(coachOfLoggedUser);
        coachRepo.save(currentOwnerOfTheCard);
    }

    public boolean hasCoachHasMoneyToDrawCard(Coach coach) {
        int drawCardCost = 50;
        return coach.getAmountMoney() >= drawCardCost;
    }

    public Card getCardOfCurrentCoachWithCard(String name) {
        return findCoachOfLoggedUser().getCards().stream()
                .filter(card -> card.getName().equals(name))
                .findFirst()
                .orElseThrow(CardNotFoundException::new);
    }

    public boolean isCoachRepoEmpty() {
        return coachRepo.count() == 0;
    }
}

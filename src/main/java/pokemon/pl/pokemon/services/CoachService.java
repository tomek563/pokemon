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
    private CoachRepo coachRepo;

    private CardRepo cardRepo;

    private CardService cardService;

    private AppUserService appUserService;

    private int drawCardCost = 50;

    public CoachService(CoachRepo coachRepo, CardRepo cardRepo, CardService cardService, AppUserService appUserService) {
        this.coachRepo = coachRepo;
        this.cardRepo = cardRepo;
        this.cardService = cardService;
        this.appUserService = appUserService;
    }

    public void addCoach(Coach coach) {
        AppUser currentUser = appUserService.getCurrentUser();
        coach.setAppUser(currentUser);
        coachRepo.save(coach);
    }

    public Coach findCoachOfLoggedUser() {
        Long principalId = getLoggedUserId();
        Optional<Coach> byAppUserId = Optional.ofNullable(coachRepo.findByAppUserId(principalId));
        Coach coach = byAppUserId.orElseThrow(() -> new CoachNotFoundException(1L));
        return coach;
    }
    public boolean hasUserGotCoach() {
        Long principalId = getLoggedUserId();
        if(coachRepo.findByAppUserId(principalId)!=null) {
            return true;
        }
        return false;
    }

    public Long getLoggedUserId() {
        AppUser currentUser = appUserService.getCurrentUser();
        return currentUser.getId();
    }


    public Coach findByCardsName(Card card) { /*test done*/
        return coachRepo.findByCardsName(card.getName());
    }
    @Scheduled(fixedRate = 3600000, initialDelay = 3600000)
    public void addMoneyAfterOneHour() {
        coachRepo.findAll()
                .forEach(coach -> {
                    coach.setAmountMoney(coach.getAmountMoney() + 100);
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

    public boolean hasCoachHasMoneyToDrawCard(Coach coach) { /*test done*/
        return coach.getAmountMoney()>=drawCardCost;
    }


    public Card getCardOfCoachWith(String name, Coach currentCoach) {
        return currentCoach.getCards().stream()
                .filter(card -> card.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CardNotFoundException());
    }

    public List<Card> getFivePokemonCardsAndPayForThem(Coach coach) {
        List<Card> sublistedCards = cardService.drawFiveRandomCards();
        sublistedCards.forEach(card -> card.setCoach(coach));

        coach.getCards().addAll(sublistedCards);
        coach.setAmountMoney(coach.getAmountMoney() - drawCardCost);
        coachRepo.save(coach);
        return sublistedCards;
    }

    public boolean isCoachRepoEmpty() {

        return coachRepo.count()==0;
    }
}

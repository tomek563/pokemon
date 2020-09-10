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

    public CoachService(CoachRepo coachRepo, CardRepo cardRepo, CardService cardService) {
        this.coachRepo = coachRepo;
        this.cardRepo = cardRepo;
        this.cardService = cardService;
    }

    public void addCoach(Coach coach) {
        AppUser principal = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        coach.setAppUser(principal);
        coachRepo.save(coach);
    }

    public Coach findCoachOfLoggedUser() {
        Long principalId = getLoggedUserId();
        Optional<Coach> byAppUserId = Optional.ofNullable(coachRepo.findByAppUserId(principalId));
        Coach coach = byAppUserId.orElseThrow(() -> new CoachNotFoundException(1L));
        return coach;
    }
    public boolean findNoCoachOfUser() {
        Long principalId = getLoggedUserId();
        if(coachRepo.findByAppUserId(principalId)==null) {
            return true;
        }
        return false;
    }

    private Long getLoggedUserId() {
        AppUser principal = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }




    public Coach findByCardsName(Card card) {
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

    public boolean coachHasMoneyToDrawCard(Coach coach) {
        int cardCost = 50;
        return coach.getAmountMoney()>=cardCost;
    }


    public Card getCardOfCoachWith(String name, Coach currentCoach) {
        return currentCoach.getCards().stream()
                .filter(card -> card.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CardNotFoundException(1L));
    }

    public List<Card> getFivePokemonCardsAndPayForThem(Coach coach) {
        List<Card> sublistedCards = cardService.drawFiveRandomCards();
        sublistedCards.forEach(card -> card.setCoach(coach));

        coach.getCards().addAll(sublistedCards);
        coach.setAmountMoney(coach.getAmountMoney() - 50);
        coachRepo.save(coach);
        return sublistedCards;
    }

    public boolean isCoachRepoEmpty() {
        return coachRepo.count()==0;
    }
}

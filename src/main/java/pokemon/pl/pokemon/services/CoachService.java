package pokemon.pl.pokemon.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.repositories.CardRepo;
import pokemon.pl.pokemon.repositories.CoachRepo;

import java.util.List;
import java.util.Timer;


@Service
public class CoachService {
    private CoachRepo coachRepo;

    private CardRepo cardRepo;

    public CoachService(CoachRepo coachRepo, CardRepo cardRepo) {
        this.coachRepo = coachRepo;
        this.cardRepo = cardRepo;
    }

    public void addCoach(Coach coach) {
        AppUser principal = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        coach.setAppUser(principal);
        coachRepo.save(coach);
    }

    public void drawCards(List<Card> cards) {
        Coach one = findCoachOfLoggedUser();
        cards.forEach(card -> card.setCoach(one));

        one.getCards().addAll(cards);
        one.setAmountMoney(one.getAmountMoney() - 50);
        coachRepo.save(one);
    }

    public Coach findCoachOfLoggedUser() {
        Long principalId = getLoggedUserId();
        return coachRepo.findByAppUserId(principalId);
    }

    private Long getLoggedUserId() {
        AppUser principal = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }

    public void saveCurrentCoach(Coach coach) {
        coachRepo.save(coach);
    }

    public Coach createCoachIfNotExist() {
        if (findCoachOfLoggedUser() == null) {
            return new Coach();
        }
        return findCoachOfLoggedUser();
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
}

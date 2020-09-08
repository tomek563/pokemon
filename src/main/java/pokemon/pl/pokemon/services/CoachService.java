package pokemon.pl.pokemon.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.repositories.CardRepo;
import pokemon.pl.pokemon.repositories.CoachRepo;

import java.util.List;


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


        one.getCards().addAll(cards.subList(0, 5));
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
}

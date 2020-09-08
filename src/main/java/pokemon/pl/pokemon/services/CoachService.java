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

        for (Card card : cards) {
            card.setCoach(one);
            System.out.println("pokaze coacha "+card.getCoach().getCoachName());
            cardRepo.save(card);
            coachRepo.save(one);
        }
        for (Card card : one.getCards()) {
            System.out.println("!!!!!!!!!!nazwa trenera");
            card.getCoach().getCoachName();
        }

        one.getCards().addAll(cards);
        one.setAmountMoney(one.getAmountMoney() - 50);
        coachRepo.save(one);
        for (Card card : one.getCards()) {
            System.out.println("po save nazwa trenera"+            card.getCoach().getCoachName());

        }
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

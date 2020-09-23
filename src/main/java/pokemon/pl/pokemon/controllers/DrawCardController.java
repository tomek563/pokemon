package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.services.CardService;
import pokemon.pl.pokemon.services.CoachService;

import java.util.List;
@Controller
public class DrawCardController {

    private CoachService coachService;

    private CardService cardService;

    public DrawCardController(CoachService coachService, CardService cardService) {
        this.coachService = coachService;
        this.cardService = cardService;
    }

    @GetMapping("/draw-random-cards")
    public String draw(Model model) {
        Coach currentCoach = coachService.findCoachOfLoggedUser();
        model.addAttribute("money", currentCoach.getAmountMoney());
        return "random-cards";
    }

    @PostMapping("/drawn")
    public String drawRandomCards(Model model) {
        Coach coach = coachService.findCoachOfLoggedUser();
        if (coachService.hasCoachHasMoneyToDrawCard(coach)) {
            List<Card> sublistedCards = coachService.getFivePokemonCardsAndPayForThem(coach);

            model.addAttribute("randomCards", sublistedCards);
            model.addAttribute("money", coach.getAmountMoney());
            return "random-cards";
        } else {
            model.addAttribute("failure", "Nie masz wystarczająco pieniędzy");
            return "failure";
        }
    }
}

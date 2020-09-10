package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pokemon.pl.pokemon.exceptions.CardNotFoundException;
import pokemon.pl.pokemon.exceptions.CoachNotFoundException;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.services.CardService;
import pokemon.pl.pokemon.services.CoachService;

import java.util.List;
import java.util.Optional;

@Controller
public class CoachController {

    private CoachService coachService;

    private CardService cardService;

    public CoachController(CoachService coachService, CardService cardService) {
        this.coachService = coachService;
        this.cardService = cardService;
    }

    @GetMapping("/pokemon")
    public String showPokemon(Model model) {
        Optional<Coach> coach = Optional.ofNullable(coachService.findCoachOfLoggedUser());
        Coach currentCoach = coach.orElseThrow(() -> new CoachNotFoundException(1L));
        model.addAttribute("cards", currentCoach.getCards());
        return "all-pokemon";
    }

    @GetMapping("/pokemon/{name}")
    public String showPokemonCard(@PathVariable String name, Model model) {
        Coach coach = coachService.findCoachOfLoggedUser();
        Optional<Card> chosenCard = Optional.of(coach.getCards().stream()
                .filter(card -> card.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CardNotFoundException(1L)));

        model.addAttribute("card", chosenCard.get());
        return "pokemon";
    }

    @GetMapping("/coach")
    public String addCoach(Model model) {
        Coach coachIfNotExist = coachService.createCoachIfNotExist();
        model.addAttribute("coach", coachIfNotExist);
        return "coach";
    }

    @PostMapping("/coach-added")
    public String saveCoach(@ModelAttribute Coach coach, Model model) {
        coachService.addCoach(coach);
        model.addAttribute("success", "Dodano nowego trenera");
        return "success";
    }

    @GetMapping("/draw-random-cards")
    public String draw(Model model) {
        Optional<Coach> coach = Optional.ofNullable(coachService.findCoachOfLoggedUser());
        Coach currentCoach = coach.orElseThrow(() -> new CoachNotFoundException(1L));
        model.addAttribute("money", currentCoach.getAmountMoney());
        return "random-cards";
    }

    @PostMapping("/drawn")
    public String drawRandomCards(Model model) {
        int cardCost = 50;
        Coach coach = coachService.findCoachOfLoggedUser();

        if (coach.getAmountMoney()>=cardCost) {
            List<Card> sublistedCards = cardService.drawFiveRandomCards();
            coachService.drawCards(sublistedCards);
            model.addAttribute("randomCards", sublistedCards);
            model.addAttribute("money", coach.getAmountMoney());
            return "random-cards";
        } else {
            model.addAttribute("failure", "Nie masz wystarczająco pieniędzy");
            return "failure";
        }
    }
}

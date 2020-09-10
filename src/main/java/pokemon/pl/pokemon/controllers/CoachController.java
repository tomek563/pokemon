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

    @GetMapping("/pokemony")
    public String pokazPokemony(Model model) {
        Optional<Coach> coach = Optional.ofNullable(coachService.findCoachOfLoggedUser());
        Coach currentCoach = coach.orElseThrow(() -> new CoachNotFoundException(1L));
        model.addAttribute("cards", currentCoach.getCards());
        return "pokemony";
    }

    @GetMapping("/pokemony/{name}")
    public String pokazKonkretnaKarte(@PathVariable String name, Model model) {
        Coach coach = coachService.findCoachOfLoggedUser();
        Optional<Card> chosenCard = Optional.of(coach.getCards().stream()
                .filter(card -> card.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CardNotFoundException(1L)));

        model.addAttribute("karta", chosenCard.get());
        return "pokemon";
    }

    @GetMapping("/trener")
    public String dodajTrenera(Model model) {
        Coach coachIfNotExist = coachService.createCoachIfNotExist();
        model.addAttribute("coach", coachIfNotExist);
        return "trener";
    }

    @PostMapping("/dodano-trenera")
    public String zapiszTrenera(@ModelAttribute Coach coach, Model model) {
        coachService.addCoach(coach);
        model.addAttribute("sukces", "Dodano nowego trenera");
        return "sukces";
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
        Coach coach = coachService.findCoachOfLoggedUser();

        List<Card> sublistedCards = cardService.drawFiveRandomCards();
        coachService.drawCards(sublistedCards);

        model.addAttribute("randomCards", sublistedCards);
        model.addAttribute("money", coach.getAmountMoney());
        return "random-cards";
    }


}

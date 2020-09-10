package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pokemon.pl.pokemon.exceptions.CardNotFoundException;
import pokemon.pl.pokemon.exceptions.CoachNotFoundException;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.services.CoachService;

import java.util.Optional;

@Controller
public class PokemonController {

    private CoachService coachService;

    public PokemonController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping("/pokemon")
    public String showPokemon(Model model) {
        Coach currentCoach = coachService.findCoachOfLoggedUser();
        model.addAttribute("cards", currentCoach.getCards());
        return "all-pokemon";
    }

    @GetMapping("/pokemon/{name}")
    public String showPokemonCard(@PathVariable String name, Model model) {
        Coach currentCoach = coachService.findCoachOfLoggedUser();
        Card chosenCard = coachService.getCardOfCoachWith(name, currentCoach);

        model.addAttribute("card", chosenCard);
        return "pokemon";
    }
}

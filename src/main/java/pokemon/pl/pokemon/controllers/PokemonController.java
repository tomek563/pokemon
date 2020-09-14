package pokemon.pl.pokemon.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pokemon.pl.pokemon.exceptions.CardNotFoundException;
import pokemon.pl.pokemon.exceptions.CoachNotFoundException;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.repositories.CardRepo;
import pokemon.pl.pokemon.services.CoachService;
import pokemon.pl.pokemon.services.PageService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class PokemonController {

    private CoachService coachService;

    private PageService pageService;

    public PokemonController(CoachService coachService, PageService pageService) {
        this.coachService = coachService;
        this.pageService = pageService;
    }

    @GetMapping("/pokemon")
    public String showPokemon(Model model, @RequestParam("page") Optional<Integer> page) {

        Page<Card> pages = pageService.buildPagesToShowPokemon(page);
        model.addAttribute("pages", pages);

        List<Integer> pageNumbers = pageService.getListOfPageNumbers(pages);
        model.addAttribute("pageNumbers", pageNumbers);

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

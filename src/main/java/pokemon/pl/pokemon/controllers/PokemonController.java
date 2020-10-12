package pokemon.pl.pokemon.controllers;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.services.CoachService;
import pokemon.pl.pokemon.services.PageService;

import java.util.List;
import java.util.Optional;

@Controller
public class PokemonController {

    private final CoachService coachService;

    private final PageService pageService;

    public PokemonController(CoachService coachService, PageService pageService) {
        this.coachService = coachService;
        this.pageService = pageService;
    }

    @GetMapping("/pokemon")
    public String showPokemon(Model model, @RequestParam("page") Optional<Integer> page) {

        Page<Card> pages = pageService.buildPagesToShowPokemon(page);
        model.addAttribute("pages", pages);

        Integer pageInteger = page.orElse(0);

        model.addAttribute("currentPage", pageInteger);

        List<Integer> pageNumbers = pageService.getListOfPageNumbers(pages);

        model.addAttribute("pageNumbers", pageNumbers);

        return "all-pokemon";
    }

    @GetMapping("/pokemon/{name}")
    public String showPokemonCard(@PathVariable String name, Model model){
        Card chosenCard = coachService.getCardOfCurrentCoachWithCard(name);

        model.addAttribute("card", chosenCard);
        return "pokemon";
    }
}

package pokemon.pl.pokemon.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.repositories.AppUserRepo;
import pokemon.pl.pokemon.repositories.CardRepo;
import pokemon.pl.pokemon.repositories.CoachRepo;
//import pokemon.pl.pokemon.repositories.MarketRepo;
import pokemon.pl.pokemon.services.CardService;
import pokemon.pl.pokemon.services.CoachService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
//        Coach coach = coachRepo.findById(1L).orElseThrow(() -> new CoachNotFoundException(1L));
        Coach coach = coachService.findCoachOfLoggedUser();

        model.addAttribute("cards", coach.getCards());
        return "pokemony";
    }
    @GetMapping("/pokemony/{name}")
    public String pokazKonkretnaKarte(@PathVariable String name, Model model) {
        Coach coach = coachService.findCoachOfLoggedUser();
        Optional<Card> pikachu = Optional.of(coach.getCards().stream()
                .filter(card -> card.getName().equals(name))
                .findFirst()
                .get());
        model.addAttribute("karta", pikachu.get());
        return "pokemon";
    }
    @PostMapping("/wystawiono")
    public String wystawNaSprzedaz(@ModelAttribute Card card) {
        Coach coach = coachService.findCoachOfLoggedUser();
        cardService.setCardOnSaleAndOwner(card, coach);
        return "sukces";
    }
    @PostMapping("/kupiono")
    public String kupKarte(@ModelAttribute Card card) {

        return "market";
    }

    @GetMapping("/trener")
    public String dodajTrenera(Model model) {
        Coach coachIfNotExist = coachService.createCoachIfNotExist();
        model.addAttribute("coach", coachIfNotExist);
        return "trener";
    }

    @GetMapping("/market")
    public String pokazMarket(Model model) {
        List<Card> cardsOnSale = cardService.showCardsOnSale();

        model.addAttribute("marketCards", cardsOnSale);
        return "market";
    }

    @PostMapping("/dodano-trenera")
    public String zapiszTrenera(@ModelAttribute Coach coach) {
        coachService.addCoach(coach);
        return "sukces";
    }

    @GetMapping("/draw-random-cards")
    public String draw(Model model) {
        Coach coach = coachService.findCoachOfLoggedUser();
        model.addAttribute("money", coach.getAmountMoney());
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
//    Optional
//    public String pokazPokemony(Model model, @PathVariable long id) {
//        Optional<Coach> coach = coachRepo.findById(id);
////        System.out.println("trener "+coach.getId());
//        if (coach.isPresent()) {
//            model.addAttribute("cards", coach);
//        }
//        return "pokemony";
//    }
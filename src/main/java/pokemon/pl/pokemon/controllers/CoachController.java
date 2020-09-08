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

    @PostMapping("/wystawiono")
    public String wystawNaSprzedaz(@ModelAttribute Card card) {
        Coach coach = coachService.findCoachOfLoggedUser();
        cardService.setCardOnSaleAndOwner(card, coach);
        return "sukces";
    }

    @PostMapping("/kupiono")
    public String kupKarte(@ModelAttribute Card card) {
        Coach currentOwnerOfTheCard = coachService.findByCardsName(card);
        Coach coachOfLoggedUser = coachService.findCoachOfLoggedUser();

        if (coachService.hasCoachEnoughMoneyToBuyCard(coachOfLoggedUser, card)) {
            coachService.finishTransaction(currentOwnerOfTheCard, coachOfLoggedUser, card);
            return "sukces";
        } else {
            return "failure";
        }
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

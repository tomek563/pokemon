package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pokemon.pl.pokemon.exceptions.CoachNotFoundException;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.services.CardService;
import pokemon.pl.pokemon.services.CoachService;

import java.util.List;
import java.util.Optional;

@Controller
public class AuctionController {

    private CoachService coachService;

    private CardService cardService;

    public AuctionController(CoachService coachService, CardService cardService) {
        this.coachService = coachService;
        this.cardService = cardService;
    }

    @GetMapping("/market")
    public String showMarket(Model model) {
        List<Card> cardsOnSale = cardService.showCardsOnSale();

        model.addAttribute("marketCards", cardsOnSale);
        return "market";
    }

    @PostMapping("/on-sale")
    public String putUpOnSale(@ModelAttribute Card card, Model model) {
        Coach coach = coachService.findCoachOfLoggedUser();
        cardService.setCardOnSaleAndOwner(card, coach);
        model.addAttribute("success", "Wystawiono kartę na sprzedaż");
        return "success";
    }

    @PostMapping("/bought")
    public String buyCard(@ModelAttribute Card card, Model model) {
        Coach currentOwnerOfTheCard = coachService.findByCardsName(card);
        Optional<Coach> coach = Optional.ofNullable(coachService.findCoachOfLoggedUser());
        Coach coachOfLoggedUser = coach.orElseThrow(() -> new CoachNotFoundException(1L));

        if (coachService.hasCoachEnoughMoneyToBuyCard(coachOfLoggedUser, card)) {
            coachService.finishTransaction(currentOwnerOfTheCard, coachOfLoggedUser, card);
            model.addAttribute("success", "Zakupiono nową kartę");
            return "success";
        } else {
            model.addAttribute("failure", "Nie masz wystarczająco pieniędzy by kupić nową kartę");
            return "failure";
        }
    }
}

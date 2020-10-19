package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pokemon.pl.pokemon.model.Card;
import pokemon.pl.pokemon.services.CardService;
import pokemon.pl.pokemon.services.CoachService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AuctionController {
    private final CoachService coachService;
    private final CardService cardService;

    public AuctionController(CoachService coachService, CardService cardService) {
        this.coachService = coachService;
        this.cardService = cardService;
    }

    @GetMapping("/market")
    public String showMarket(Model model) {
        List<Card> cardsOnSale = cardService.getCardsOnSale();
        model.addAttribute("marketCards", cardsOnSale);
        return "market";
    }

    @PostMapping("/on-sale")
    public String putUpOnSale(@Valid @ModelAttribute Card card, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "pokemon";
        }
        cardService.setCardOnSaleAndOwner(card);
        model.addAttribute("successMessage", "Wystawiono kartę na sprzedaż");
        return "success";
    }

    @PostMapping("/bought")
    public String buyCard(@ModelAttribute Card card, Model model) {
        if (coachService.hasCoachEnoughMoneyToBuyCard(card)) {
            coachService.finishTransaction(card);
            model.addAttribute("successMessage", "Zakupiono nową kartę");
            return "success";
        } else {
            model.addAttribute("errorMessage", "Nie masz wystarczająco pieniędzy by kupić nową kartę");
            return "failure";
        }
    }
}

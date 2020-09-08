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

    private CoachRepo coachRepo;

    private CardRepo cardRepo;

//    private MarketRepo marketRepo;

    private CardService cardService;

    public CoachController(CoachService coachService, CoachRepo coachRepo, CardRepo cardRepo/*, MarketRepo marketRepo*/, CardService cardService) {
        this.coachService = coachService;
        this.coachRepo = coachRepo;
        this.cardRepo = cardRepo;
//        this.marketRepo = marketRepo;
        this.cardService = cardService;
    }

    @GetMapping("/pokemony")
    public String pokazPokemony(Model model) {
//        Coach coach = coachRepo.findById(1L).orElseThrow(() -> new CoachNotFoundException(1L));
        Coach coach = coachService.findCoachOfLoggedUser();
        System.out.println(coach.getCoachName());
        System.out.println(coach.getCards());
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

        System.out.println("nazwa karty "+card.getName());
        System.out.println("id "+card.getId());
        System.out.println("url "+card.getUrl());

        System.out.println("cena "+card.getPrice());
        Coach coach = coachService.findCoachOfLoggedUser();
        card.setOnSale(true);
        card.setCoach(coach);
        System.out.println("ustawia sie na true "+card.isOnSale());
//        if (coach.getCards().contains(card)) {
//            coach.getCards().get().setOnSale(true);
//            coach.getCards().remove(card);
//        }
        cardRepo.save(card);
        coachRepo.save(coach);
//        marketRepo.getOne(1L).getCards().add(card);
//        marketRepo.save(marketRepo.getOne(1L));
        return "sukces";
    }
    @PostMapping("/kupiono")
    public String kupKarte(@ModelAttribute Card card) {
        System.out.println("kupiono "+card.getName());
        System.out.println("cena "+card.getPrice());
        return "market";
    }

    @GetMapping("/dodaj-trenera")
    public String dodajTrenera(Model model) {
        model.addAttribute("coach", new Coach());
        return "nowy-trener";
    }

    @GetMapping("/market")
    public String pokazMarket(Model model) {
        List<Card> collect = Stream.of(cardRepo.findAll())
                .flatMap(coaches -> coaches.stream())
                .filter(card -> card.isOnSale())
                .collect(Collectors.toList());
        System.out.println("tu jest cala lista"+collect);
//        System.out.println("trener nazywa sie"+collect.get(1).getCoach().getCoachName());
        model.addAttribute("marketCards", collect);
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
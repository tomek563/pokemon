//package pokemon.pl.pokemon.controllers;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import pokemon.pl.pokemon.model.Market;
//import pokemon.pl.pokemon.services.MarketService;
//
//@Controller
//public class MarketController {
//    private MarketService marketService;
//
//    public MarketController(MarketService marketService) {
//        this.marketService = marketService;
//    }
//
//    @GetMapping("/market")
//    public String showMarket(Model model) {
//        model.addAttribute("market", new Market());
//        model.addAttribute("marketCards",marketService.showCardsInMarket());
//        return "market";
//    }
//}

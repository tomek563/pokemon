package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.services.CoachService;

@Controller
public class IndexController {

    @GetMapping({"/index", "/index.html", ""})
    public String sayHello() {
        return "index";
    }

    @GetMapping("/layout")
    public String layout(Model model) {

        return "layout";
    }

    @GetMapping("/success")
    public String loginSuccess(@RequestParam(defaultValue = "true") boolean firstTime, Model model) {
        if (firstTime) {
            model.addAttribute("success", "Zostałeś poprawnie zalogowany"+
                    ". Najpierw wejdź w zakładke 'trener' i stwórz nowego trenera, dzięki temu będziesz mógł rozpocząć kolekcjonowanie kart!");
        } else {
            model.addAttribute( "success", "Zostałeś poprawnie zalogowany");
        }
        return "success";
    }

    @GetMapping("/failure")
    public String loginFailure(Model model) {
        model.addAttribute("failure", "Nie udało się zalogować. Sprawdź nazwę użytkownika i hasło ");
        return "failure";
    }

    @GetMapping("/no-coach")
    public String showNoCoach() {

        return "no-coach";
    }
    @GetMapping("/no-card")
    public String showNoCard() {

        return "no-card";
    }


}

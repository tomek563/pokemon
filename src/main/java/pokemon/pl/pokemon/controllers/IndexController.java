package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/sukces")
    public String loginSuccess(Model model) {
        model.addAttribute("sukces", "Zostałeś poprawnie zalogowany");
        return "sukces";
    }

    @GetMapping("/porazka")
    public String loginFailure(Model model) {
        model.addAttribute("failure", "Nie udało się zalogować. Sprawdź nazwę użytkownika i hasło ");
        return "failure";
    }

    @GetMapping("/brak-trenera")
    public String pokazWyjatekTrener() {

        return "brak-trenera";
    }
    @GetMapping("/brak-karty")
    public String pokazWyjatekKarta() {

        return "brak-karty";
    }


}

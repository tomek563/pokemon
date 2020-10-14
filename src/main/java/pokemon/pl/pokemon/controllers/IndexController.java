package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {

    @GetMapping({"/index", "/index.html", ""})
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/layout")
    public String getLayout() {
        return "layout";
    }

    @GetMapping("/failure")
    public String getLoginFailure(Model model) {
        model.addAttribute("errorMessage", "Nie udało się zalogować. Sprawdź nazwę użytkownika i hasło ");
        return "failure";
    }

    @GetMapping("/no-coach")
    public String getNoCoach() {
        return "no-coach";
    }

    @GetMapping("/no-card")
    public String getNoCard() {
        return "no-card";
    }


}

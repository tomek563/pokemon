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
    public String loginSuccess() {
        return "sukces";
    }

    @GetMapping("/porazka")
    public String loginFailure() {
        return "failure";
    }


}

package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping({"/index", "/index.html", ""})
    public String sayHello() {
        return "index";
    }

    @GetMapping("/layout")
    public String layout() {
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

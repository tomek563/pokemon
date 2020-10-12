package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Token;
import pokemon.pl.pokemon.repositories.AppUserRepo;
import pokemon.pl.pokemon.repositories.TokenRepo;
import pokemon.pl.pokemon.services.TokenService;

@Controller
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/token")
    public String signUp(@RequestParam String value) {
        tokenService.validateAppUser(value);
        return "index";
    }
}

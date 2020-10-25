package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import pokemon.pl.pokemon.model.AppUser;

import pokemon.pl.pokemon.services.AppUserService;

import javax.validation.Valid;
import java.util.*;


@Controller
public class AppUserController {
    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "sign-up";
    }

    @PostMapping("/register") //
    public String register(@Valid @ModelAttribute AppUser appUser,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "sign-up";
        } else {
            appUserService.addUser(appUser);
            model.addAttribute("successMessage","Dodano nowego użytkownika. Zaloguj się");
            return "success";
        }
    }


}

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
    private AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "sign-up";
    }
//// pierwsza opcja walidacji: wyskakuje 'bad request' /*to bardziej do restApi*/
//    @PostMapping("/register") //
//    public ResponseEntity<String> register(@Valid AppUser appUser) {
//            userService.addUser(appUser);
//            return ResponseEntity.ok("Udało się zarejestrowac użytkownika");
//        }

// druga opcja walidacji: wyskakuje informacja co jest wpisane niepoprawnie
/*@Modelattribute mozliwe przekazanie z widoku do POJO*/
@PostMapping("/register") //
    public String register(@Valid @ModelAttribute AppUser appUser,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errorsList = bindingResult.getAllErrors();
            for (ObjectError error : errorsList) {
                System.out.println(error);
            }
            return "sign-up";
        } else {
            appUserService.addUser(appUser);
            model.addAttribute("success","Dodano nowego użytkownika. Zaloguj się");
            return "success";
        }
    }


}

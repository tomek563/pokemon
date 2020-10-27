package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pokemon.pl.pokemon.model.AppUser;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.services.AppUserService;
import pokemon.pl.pokemon.services.CoachService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CoachController {
    private final CoachService coachService;

    public CoachController(CoachService coachService, AppUserService appUserService) {
        this.coachService = coachService;
    }

    @GetMapping("/coach")
    public String getCoachPage(Model model) {
        if (coachService.hasUserGotCoach()) {
            Coach coach = coachService.findCoachOfLoggedUser();
            model.addAttribute("coach", coach);
            return "coach";
        } else {
            Coach coach = new Coach();
            model.addAttribute("coach", coach);
            return "add-coach";
        }
    }

    @PostMapping("/coach-added")
    public String saveCoach(@Valid @ModelAttribute Coach coach, BindingResult bindingResult,
                            Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()||coachService.hasUserGotCoach()) {
            List<ObjectError> errorsList = bindingResult.getAllErrors();
            return "add-coach";
        } else {
            coachService.addCoach(coach);
            model.addAttribute("successMessage", "Dodano nowego trenera");
            redirectAttributes.addFlashAttribute("redirect", true);
            return "redirect:draw-random-cards";
        }
    }
}

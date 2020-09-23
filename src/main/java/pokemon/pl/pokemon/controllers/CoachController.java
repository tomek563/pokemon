package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pokemon.pl.pokemon.model.Coach;
import pokemon.pl.pokemon.services.CoachService;

@Controller
public class CoachController {

    private CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }


    //przekazywanie danych przez redirect
    //1. RedirectAtributes dajesz addAtribute, zgarniasz po argumentach
    //2. -||- addFlashAtribute, zgarniasz z modelu
    @GetMapping("/coach") /*!!!!!! JAK MOZNA TO INACZEJ ZREFACTOROWAC??*/
    public String getCoachPage(Model model) {
        if (coachService.hasUserGotCoach()) {
            Coach coach = coachService.findCoachOfLoggedUser();
            model.addAttribute("coach", coach);
        } else {
            Coach coach = new Coach();
            model.addAttribute("coach", coach);
        }
        return "coach";
    }

    @PostMapping("/coach-added")
    public String saveCoach(@ModelAttribute Coach coach, Model model) {
        coachService.addCoach(coach);
        model.addAttribute("success", "Dodano nowego trenera");
        return "success";
    }

}

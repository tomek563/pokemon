package pokemon.pl.pokemon.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pokemon.pl.pokemon.services.CoachService;

@Controller
public class LoginController {
    private CoachService coachService;

    public LoginController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping("/success")
    public String loginSuccess(Model model, RedirectAttributes redirectAttributes) {

        if (coachService.hasUserGotCoach()) {
            model.addAttribute( "success", "Zostałeś poprawnie zalogowany");
        } else {
            redirectAttributes.addFlashAttribute("redirect", true);
            return "redirect:coach";
        }
        return "success";
    }
}

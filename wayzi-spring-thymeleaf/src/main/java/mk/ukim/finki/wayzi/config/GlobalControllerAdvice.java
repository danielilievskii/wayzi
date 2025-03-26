package mk.ukim.finki.wayzi.config;

import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.service.StandardUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final StandardUserService standardUserService;

    public GlobalControllerAdvice(StandardUserService standardUserService) {
        this.standardUserService = standardUserService;
    }

    @ModelAttribute
    public void addAuthPassenger(Model model, @AuthenticationPrincipal StandardUser standardUser) {
        if (standardUser != null) {
            model.addAttribute("authUser", standardUser);
        }
    }
}

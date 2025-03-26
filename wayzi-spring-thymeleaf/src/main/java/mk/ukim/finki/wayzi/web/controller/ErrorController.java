package mk.ukim.finki.wayzi.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ErrorController {

    @GetMapping("/error-page")
    public String getErrorPage(Model model, @ModelAttribute String errorMessage) {
        if(errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", errorMessage);
        }
        return "error-page";
    }
}

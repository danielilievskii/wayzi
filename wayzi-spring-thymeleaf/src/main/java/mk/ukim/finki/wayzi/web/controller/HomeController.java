package mk.ukim.finki.wayzi.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHomePage(@RequestParam(required = false) String error,
                              Model model) {

        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        LocalDate date = LocalDate.now();
        model.addAttribute("date", date);
        model.addAttribute("passengersNum", 1);


        return "home";
    }
}

package mk.ukim.finki.wayzi.web.controller;

import jakarta.validation.Valid;
import mk.ukim.finki.wayzi.exception.UserAlreadyExistsException;
import mk.ukim.finki.wayzi.dto.SignUpDto;
import mk.ukim.finki.wayzi.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/signup")
    public String getSignUpPage(@RequestParam(required = false) String error,
                                  Model model) {

        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        model.addAttribute("signup", new SignUpDto());

        model.addAttribute("bodyContent", "/auth/signup-form");
        return "master-template";
    }

    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute(value = "signup") SignUpDto signUpDto,
                         BindingResult result,
                         Model model
    ) {
        if(result.hasErrors()) {
            model.addAttribute("bodyContent", "/auth/signup-form");
            return "master-template";
        }

        try {
            authService.signUp(signUpDto);
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("error", "Email already in use. Please try a different one.");
            model.addAttribute("bodyContent", "/auth/signup-form");
            return "master-template";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) String error,
                                Model model) {

        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "/auth/login-form");

        return "master-template";
    }

}

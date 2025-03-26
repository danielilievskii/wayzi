package mk.ukim.finki.wayzi.web.controller;

import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.service.AuthService;
import mk.ukim.finki.wayzi.service.StandardUserService;
import mk.ukim.finki.wayzi.service.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ProfileController {

    private final VehicleService vehicleService;
    private final AuthService authService;
    private final StandardUserService standardUserService;

    public ProfileController(VehicleService vehicleService, AuthService authService, StandardUserService standardUserService) {
        this.vehicleService = vehicleService;
        this.authService = authService;
        this.standardUserService = standardUserService;
    }


    @GetMapping("/profile")
    public String getProfilePage(@RequestParam(required = false) String error,
                              Model model) {

        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        model.addAttribute("bodyContent", "profile");

        StandardUser standardUser = authService.getAuthenticatedStandardUser();
        model.addAttribute("standardUser", standardUser);

        List<Vehicle> vehicles = vehicleService.findAllForAuthenticatedUser();
        model.addAttribute("vehicles", vehicles);

        return "master-template";
    }

    @PostMapping("/profile/upload-profile-pic")
    public String uploadProfilePic(@RequestParam MultipartFile file) {

        standardUserService.uploadProfilePic(file);
        return "redirect:/profile";
    }

}

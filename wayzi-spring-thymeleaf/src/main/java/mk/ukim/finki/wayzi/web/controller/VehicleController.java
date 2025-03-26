package mk.ukim.finki.wayzi.web.controller;

import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.dto.CreateVehicleDto;
import mk.ukim.finki.wayzi.service.AuthService;
import mk.ukim.finki.wayzi.service.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final AuthService authService;

    public VehicleController(VehicleService vehicleService, AuthService authService) {
        this.vehicleService = vehicleService;
        this.authService = authService;
    }


    @GetMapping("/add")
    public String getVehicleAddPage(@ModelAttribute("errorMessage") String errorMessage,
                                    Model model
    ) {
        if(errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", errorMessage);
        }

        model.addAttribute("vehicle", new CreateVehicleDto());

        model.addAttribute("bodyContent", "vehicles/add-vehicle-form");
        return "master-template";
    }

    @GetMapping("/edit/{id}")
    public String getVehicleEditPage(@ModelAttribute("errorMessage") String errorMessage,
                                       @PathVariable Long id,
                                        Model model
    ) {
        if(errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", errorMessage);
        }

        Vehicle vehicle = vehicleService.findByIdAndCheckOwnership(id);
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("vehicleId", vehicle.getId());

        model.addAttribute("bodyContent", "vehicles/edit-vehicle-form");
        return "master-template";
    }

    @PostMapping("/add")
    public String addVehicle(@Valid @ModelAttribute("vehicle") CreateVehicleDto createVehicleDto,
                             BindingResult bindingResult,
                             Model model
    ) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("bodyContent", "vehicles/add-vehicle-form");
            return "master-template";
        }

        vehicleService.save(createVehicleDto);
        return "redirect:/profile";
    }

    @PostMapping("/edit/{id}")
    public String editVehicle(@PathVariable Long id,
                              @Valid @ModelAttribute("vehicle") CreateVehicleDto createVehicleDto,
                              BindingResult bindingResult,
                              Model model
    ) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("bodyContent", "vehicles/edit-vehicle-form");
            model.addAttribute("vehicleId", id);
            return "master-template";
        }

        vehicleService.update(id, createVehicleDto);
        return "redirect:/profile";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Long id) {
        vehicleService.delete(id);
        return "redirect:/profile";
    }

}

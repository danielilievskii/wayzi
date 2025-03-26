package mk.ukim.finki.wayzi.web.controller;

import jakarta.validation.Valid;
import mk.ukim.finki.wayzi.dto.DisplayRideDto;
import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.dto.CreateRideDto;
import mk.ukim.finki.wayzi.converter.RideConverter;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.service.RideService;
import mk.ukim.finki.wayzi.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/rides")
public class RideController {

    private final VehicleService vehicleService;
    private final RideService rideService;

    public RideController(VehicleService vehicleService, RideService rideService) {
        this.vehicleService = vehicleService;
        this.rideService = rideService;
    }

    @GetMapping
    public String getRidesPage(
            @ModelAttribute("errorMessage") String errorMessage,
            @RequestParam(required = false) Long departureLocationId,
            @RequestParam(required = false) String departureLocationName,
            @RequestParam(required = false) Long arrivalLocationId,
            @RequestParam(required = false) String arrivalLocationName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false, defaultValue = "1") Integer passengersNum,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
                               Model model) {

        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", errorMessage);
        }

        final LocalDate date1;
        if (date == null) {
            date1 = LocalDate.now();
        } else date1 = date;

        List<Ride> rides = rideService.findAll();
        model.addAttribute("rides", rides);

        Page<Ride> page = this.rideService.findPage(departureLocationId, arrivalLocationId, date1, passengersNum, pageNum, pageSize);


        List<Ride> ridesOnDate = page.getContent().stream()
                .filter(ride -> ride.getDepartureTime().toLocalDate().equals(date1))
                .toList();

        List<Ride> ridesAfterDate = page.getContent().stream()
                .filter(ride -> ride.getDepartureTime().toLocalDate().isAfter(date1))
                .toList();

        model.addAttribute("ridesOnDate", ridesOnDate);
        model.addAttribute("ridesAfterDate", ridesAfterDate);

        model.addAttribute("page", page);
        model.addAttribute("departureLocationId", departureLocationId);
        model.addAttribute("departureLocationName", departureLocationName);
        model.addAttribute("arrivalLocationId", arrivalLocationId);
        model.addAttribute("arrivalLocationName", arrivalLocationName);
        model.addAttribute("date", date1);
        model.addAttribute("passengersNum", passengersNum);


        model.addAttribute("bodyContent", "ride/rides");

        return "master-template";
    }

    @GetMapping("/details/{id}")
    public String getRideDetailsPage(@ModelAttribute("errorMessage") String errorMessage,
                                     @PathVariable Long id,
                                     Model model) {

        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", errorMessage);
        }

        Ride ride = rideService.findById(id);
        model.addAttribute("ride", ride);

        model.addAttribute("bodyContent", "ride/ride-details");

        return "master-template";
    }

    @GetMapping("/add")
    public String getRideAddPage(@ModelAttribute("errorMessage") String errorMessage,
                                 Model model) {

        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", errorMessage);
        }

        List<Vehicle> vehicles = vehicleService.findAllForAuthenticatedUser();
        model.addAttribute("vehicles", vehicles);

        model.addAttribute("ride", new CreateRideDto());

        model.addAttribute("bodyContent", "ride/add-ride-form");
        return "master-template";
    }

    @PostMapping("/add")
    public String addRide(@Valid @ModelAttribute("ride") CreateRideDto createRideDto,
                          BindingResult bindingResult,
                          Model model) {

        if (bindingResult.hasErrors()) {
            List<Vehicle> vehicles = vehicleService.findAllForAuthenticatedUser();
            model.addAttribute("vehicles", vehicles);
            model.addAttribute("rideStops", createRideDto.getRideStops());

            model.addAttribute("bodyContent", "ride/add-ride-form");
            return "master-template";
        }

        rideService.save(createRideDto);

        return "redirect:/rides/published";
    }

    @GetMapping("/edit/{id}")
    public String getRideEditPage(@PathVariable Long id,
                                  Model model) {

        List<Vehicle> vehicles = vehicleService.findAllForAuthenticatedUser();
        model.addAttribute("vehicles", vehicles);

        Ride ride = rideService.findByIdAndCheckOwnership(id);
        model.addAttribute("ride", RideConverter.toDisplayRideDtoFrom(ride));
        model.addAttribute("rideStops", RideConverter.toDisplayRideStopDtoListFrom(ride.getRideStops()));
        model.addAttribute("rideId", ride.getId());

        model.addAttribute("bodyContent", "ride/edit-ride-form");
        return "master-template";
    }

    @PostMapping("/edit/{id}")
    public String editPage(@PathVariable Long id,
                           @Valid @ModelAttribute("ride") DisplayRideDto displayRideDto,
                           BindingResult bindingResult,
                           Model model) {

        if (bindingResult.hasErrors()) {
            List<Vehicle> vehicles = vehicleService.findAllForAuthenticatedUser();
            model.addAttribute("vehicles", vehicles);

            model.addAttribute("rideStops", displayRideDto.getRideStops());
            model.addAttribute("rideId", id);

            model.addAttribute("bodyContent", "ride/edit-ride-form");
            return "master-template";
        }

        rideService.edit(id, displayRideDto);
        return "redirect:/rides/published";

    }

    @GetMapping("/booked")
    public String getBookedRidesPage(@ModelAttribute("errorMessage") String errorMessage,
                                     Model model) {

        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", errorMessage);
        }

        model.addAttribute("bodyContent", "ride/booked-rides");

        return "master-template";
    }


    @GetMapping("/published")
    public String getPublishedRidesPage(@ModelAttribute("errorMessage") String errorMessage,
                                        Model model) {

        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("errorMessage", errorMessage);
        }

        List<Ride> publishedRides = rideService.findAllForAuthenticatedUser();
        model.addAttribute("publishedRides", publishedRides);

        model.addAttribute("bodyContent", "ride/published-rides");

        return "master-template";
    }

    @PostMapping("/confirm/{id}")
    public String confirmRide(@PathVariable Long id) {

        rideService.confirmRide(id);

        return "redirect:/rides/published";
    }


}

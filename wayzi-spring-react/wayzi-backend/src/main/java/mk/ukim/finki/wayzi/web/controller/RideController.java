package mk.ukim.finki.wayzi.web.controller;

import mk.ukim.finki.wayzi.web.dto.CreateRideDto;
import mk.ukim.finki.wayzi.web.dto.RideFilterDto;
import mk.ukim.finki.wayzi.web.dto.UpdateRideDto;
import mk.ukim.finki.wayzi.service.application.RideApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideApplicationService rideApplicationService;

    public RideController(RideApplicationService rideApplicationService) {
        this.rideApplicationService = rideApplicationService;
    }

//    @GetMapping
//    public ResponseEntity<?> findAll() {
//        return ResponseEntity.ok(rideApplicationService.findAll());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rideApplicationService.findById(id));
    }

    @GetMapping
    public ResponseEntity<?> findPage(@ModelAttribute RideFilterDto rideFilterDto) {
        return ResponseEntity.ok(rideApplicationService.findPage(rideFilterDto));
    }

    @GetMapping("/published")
    public ResponseEntity<?> findPublishedRidesPage(@ModelAttribute RideFilterDto rideFilterDto) {
        return ResponseEntity.ok(rideApplicationService.findCurrentUserPublishedRidesPage(rideFilterDto));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRide(@RequestBody CreateRideDto createRideDto) {
        return ResponseEntity.ok(rideApplicationService.save(createRideDto));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<?> addRide(@PathVariable Long id, @RequestBody UpdateRideDto updateRideDto) {
        return ResponseEntity.ok(rideApplicationService.edit(id, updateRideDto));
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<?> confirmRide(@PathVariable Long id) {
        rideApplicationService.confirmRide(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelRide(@PathVariable Long id) {
        rideApplicationService.cancelRide(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/start/{id}")
    public ResponseEntity<?> startRide(@PathVariable Long id) {
        rideApplicationService.startRide(id);
        return ResponseEntity.ok().build();
    }
}

package mk.ukim.finki.wayzi.web.controller;

import mk.ukim.finki.wayzi.service.application.RideStatusApplicationService;
import mk.ukim.finki.wayzi.web.dto.CreateRideDto;
import mk.ukim.finki.wayzi.web.dto.RideFilterDto;
import mk.ukim.finki.wayzi.web.dto.UpdateRideDto;
import mk.ukim.finki.wayzi.service.application.RideApplicationService;
import mk.ukim.finki.wayzi.web.dto.UpdateRideStatusDto;
import mk.ukim.finki.wayzi.web.dto.ride.PublishedRideFilterDto;
import mk.ukim.finki.wayzi.web.dto.ride.RideDetailsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideApplicationService rideApplicationService;
    private final RideStatusApplicationService rideStatusApplicationService;

    public RideController(RideApplicationService rideApplicationService, RideStatusApplicationService rideStatusApplicationService) {
        this.rideApplicationService = rideApplicationService;
        this.rideStatusApplicationService = rideStatusApplicationService;
    }

//    @GetMapping
//    public ResponseEntity<?> findAll() {
//        return ResponseEntity.ok(rideApplicationService.findAll());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<RideDetailsDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rideApplicationService.findById(id));
    }

    @GetMapping("/{id}/route")
    public ResponseEntity<List<List<Double>>> findRouteCoordinatesById(@PathVariable Long id) {
        return ResponseEntity.ok(rideApplicationService.findRouteCoordinatesById(id));
    }

    @GetMapping
    public ResponseEntity<?> findPage(@ModelAttribute RideFilterDto rideFilterDto) {
        return ResponseEntity.ok(rideApplicationService.findPage(rideFilterDto));
    }

    @GetMapping("/published")
    public ResponseEntity<?> findPublishedRidesPage(@ModelAttribute PublishedRideFilterDto publishedRideFilterDto) {
        return ResponseEntity.ok(rideApplicationService.findPublishedRidesPageForUser(publishedRideFilterDto));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRide(@RequestBody CreateRideDto createRideDto) {
        return ResponseEntity.ok(rideApplicationService.save(createRideDto));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<?> addRide(@PathVariable Long id, @RequestBody UpdateRideDto updateRideDto) {
        return ResponseEntity.ok(rideApplicationService.edit(id, updateRideDto));
    }

    @PostMapping("/update-status")
    public ResponseEntity<?> updateRideStatus(@RequestBody UpdateRideStatusDto updateRideStatusDto) {        ;
        return ResponseEntity.ok(rideStatusApplicationService.updateRideStatus(updateRideStatusDto));
    }

}

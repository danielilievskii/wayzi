package mk.ukim.finki.wayzi.web.controller;

import mk.ukim.finki.wayzi.service.application.RideStatusApplicationService;
import mk.ukim.finki.wayzi.web.dto.ride.*;
import mk.ukim.finki.wayzi.service.application.RideApplicationService;
import mk.ukim.finki.wayzi.web.dto.ridebooking.RideBookersDto;
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

    @GetMapping
    public ResponseEntity<RidePageDto> findPage(@ModelAttribute RideFilterDto rideFilterDto) {
        return ResponseEntity.ok(rideApplicationService.findPage(rideFilterDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideDetailsDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rideApplicationService.findById(id));
    }

    @GetMapping("/{id}/route")
    public ResponseEntity<List<List<Double>>> findRouteCoordinatesById(@PathVariable Long id) {
        return ResponseEntity.ok(rideApplicationService.findRouteCoordinatesById(id));
    }

    @GetMapping("/published")
    public ResponseEntity<RidePageDto> findPublishedRidesPage(@ModelAttribute PublishedRideFilterDto publishedRideFilterDto) {
        return ResponseEntity.ok(rideApplicationService.findPublishedRidesPageForUser(publishedRideFilterDto));
    }

    @GetMapping("/published/{id}/bookings")
    public ResponseEntity<RideBookersDto> findBookersById(@PathVariable Long id) {
        return ResponseEntity.ok(rideApplicationService.findBookersById(id));
    }

    @PostMapping
    public ResponseEntity<DisplayRideDto> createRide(@RequestBody CreateRideDto createRideDto) {
        return ResponseEntity.ok(rideApplicationService.save(createRideDto));
    }

    @PutMapping("/published/{id}/edit")
    public ResponseEntity<DisplayRideDto> updateRide(@PathVariable Long id, @RequestBody UpdateRideDto updateRideDto) {
        return ResponseEntity.ok(rideApplicationService.edit(id, updateRideDto));
    }

    @PutMapping("/update-status")
    public ResponseEntity<DisplayRideDto> updateRideStatus(@RequestBody UpdateRideStatusDto updateRideStatusDto) {        ;
        return ResponseEntity.ok(rideStatusApplicationService.updateRideStatus(updateRideStatusDto));
    }

}

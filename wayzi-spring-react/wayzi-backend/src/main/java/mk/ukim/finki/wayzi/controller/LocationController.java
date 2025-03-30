package mk.ukim.finki.wayzi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wayzi.dto.CreateVehicleDto;
import mk.ukim.finki.wayzi.service.application.LocationApplicationService;
import mk.ukim.finki.wayzi.service.application.VehicleApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationApplicationService locationApplicationService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(locationApplicationService.findAll());
    }


}


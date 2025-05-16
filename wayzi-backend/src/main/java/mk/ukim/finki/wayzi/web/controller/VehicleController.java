package mk.ukim.finki.wayzi.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wayzi.web.dto.CreateVehicleDto;
import mk.ukim.finki.wayzi.service.application.VehicleApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VehicleController {

    private final VehicleApplicationService vehicleApplicationService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdAndCheckOwnership(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleApplicationService.findByIdAndCheckOwnership(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAllForAuthenticatedUser() {
        return ResponseEntity.ok(vehicleApplicationService.findAllForUser());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addVehicle(@Valid @RequestBody CreateVehicleDto createVehicleDto) {
        return ResponseEntity.ok(vehicleApplicationService.save(createVehicleDto));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<?> editVehicle(@PathVariable Long id,
                              @Valid @RequestBody CreateVehicleDto createVehicleDto
    ) {
        return ResponseEntity.ok(vehicleApplicationService.update(id, createVehicleDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
        vehicleApplicationService.delete(id);
        return ResponseEntity.ok().build();
    }
}

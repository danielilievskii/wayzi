package mk.ukim.finki.wayzi.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wayzi.web.dto.vehicle.CreateVehicleDto;
import mk.ukim.finki.wayzi.service.application.VehicleApplicationService;
import mk.ukim.finki.wayzi.web.dto.vehicle.DisplayVehicleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VehicleController {

    private final VehicleApplicationService vehicleApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayVehicleDto>> findAllForAuthenticatedUser() {
        return ResponseEntity.ok(vehicleApplicationService.findAllForUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayVehicleDto> findByIdAndCheckOwnership(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleApplicationService.findByIdAndCheckOwnership(id));
    }

    @PostMapping("/add")
    public ResponseEntity<DisplayVehicleDto> addVehicle(@Valid @RequestBody CreateVehicleDto createVehicleDto) {
        return ResponseEntity.ok(vehicleApplicationService.save(createVehicleDto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<DisplayVehicleDto> editVehicle(@PathVariable Long id, @Valid @RequestBody CreateVehicleDto createVehicleDto) {
        return ResponseEntity.ok(vehicleApplicationService.update(id, createVehicleDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleApplicationService.delete(id);
        return ResponseEntity.ok().build();
    }
}

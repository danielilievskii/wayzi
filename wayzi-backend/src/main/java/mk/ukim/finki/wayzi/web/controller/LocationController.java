package mk.ukim.finki.wayzi.web.controller;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wayzi.service.application.LocationApplicationService;
import mk.ukim.finki.wayzi.web.dto.location.DisplayLocationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationApplicationService locationApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayLocationDto>> findAll() {
        return ResponseEntity.ok(locationApplicationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayLocationDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(locationApplicationService.findById(id));
    }
}


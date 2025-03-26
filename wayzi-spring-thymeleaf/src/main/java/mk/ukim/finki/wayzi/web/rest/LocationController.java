package mk.ukim.finki.wayzi.web.rest;

import mk.ukim.finki.wayzi.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchLocations(@RequestParam String query) {
        return ResponseEntity.ok(locationService.findAllByDisplayNameContaining(query));
    }
}

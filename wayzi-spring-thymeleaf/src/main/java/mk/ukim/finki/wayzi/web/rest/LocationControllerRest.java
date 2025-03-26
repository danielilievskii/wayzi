package mk.ukim.finki.wayzi.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("/api/locations/v2")
public class LocationControllerRest {

    @GetMapping
    public ResponseEntity<List<LocationResponse>> getLocations(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + query + "&accept-language=en";
        RestTemplate restTemplate = new RestTemplate();

        try {
            LocationResponse[] locations = restTemplate.getForObject(url, LocationResponse[].class);
            if (locations != null) {
                return ResponseEntity.ok(Arrays.asList(locations));
            } else {
                return ResponseEntity.status(500).body(Collections.singletonList(new LocationResponse("Error", "No locations found")));
            }

        }catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonList(new LocationResponse("Error", e.getMessage())));
        }
    }

    static class LocationResponse {
        @JsonProperty("name")
        public String name;

        @JsonProperty("display_name")
        public String displayName;

        public LocationResponse(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
        }


    }
}

package mk.ukim.finki.wayzi.web.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api/routes")
public class MapController {

    @Value("${openrouteservice.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/directions")
    @ResponseBody
    public ResponseEntity<String> getDirections(
            @RequestParam double startLat, @RequestParam double startLng,
            @RequestParam double endLat, @RequestParam double endLng) {

        String url = String.format(
                "https://api.openrouteservice.org/v2/directions/driving-car?api_key=%s&start=%f,%f&end=%f,%f",
                apiKey, startLng, startLat, endLng, endLat
        );

        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }
}

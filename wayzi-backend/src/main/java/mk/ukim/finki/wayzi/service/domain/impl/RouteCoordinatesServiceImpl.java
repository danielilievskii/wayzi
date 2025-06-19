package mk.ukim.finki.wayzi.service.domain.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.repository.RideRepository;
import mk.ukim.finki.wayzi.service.domain.RouteCoordinatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteCoordinatesServiceImpl implements RouteCoordinatesService {

    @Value("${openrouteservice.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    private final RideRepository rideRepository;

    public RouteCoordinatesServiceImpl(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }


    @Override
    public List<List<Double>> fetchCoordinates(List<Location> locations)  {

        List<List<Double>> coordinates = new ArrayList<>();

        for(int i=0; i<locations.size()-1; i++){
            Location startLocation = locations.get(i);
            Location endLocation = locations.get(i+1);

            String url = String.format(
                    "https://api.openrouteservice.org/v2/directions/driving-car?api_key=%s&start=%f,%f&end=%f,%f",
                    apiKey, startLocation.getLongitude(), startLocation.getLatitude(), endLocation.getLongitude(), endLocation.getLatitude()
            );

            try {
                String jsonResponse = restTemplate.getForObject(url, String.class);
                List<List<Double>> extractedCoordinates = extractCoordinates(jsonResponse);
                coordinates.addAll(extractedCoordinates);
            } catch (Exception e) {
                System.err.printf("Failed to fetch route between (%s) and (%s): %s%n",
                        startLocation.getDisplayName(), endLocation.getDisplayName(), e.getMessage());

                //TODO: Fallback retry mechanism
                return List.of();
            }
        }

        return coordinates;
    }

    public List<List<Double>> extractCoordinates(String jsonResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonResponse);

        JsonNode coordinatesNode = root
                .path("features")
                .get(0)
                .path("geometry")
                .path("coordinates");

        List<List<Double>> coordinates = new ArrayList<>();

        if(coordinatesNode.isArray()){
            for(JsonNode coord: coordinatesNode){
                double lng = coord.get(0).asDouble();
                double lat = coord.get(1).asDouble();
                List<Double> latLng = List.of(lat, lng);
                coordinates.add(latLng);
            }
        }

        return coordinates;
    }
}

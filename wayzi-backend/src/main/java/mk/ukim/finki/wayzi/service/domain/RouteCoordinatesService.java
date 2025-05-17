package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.model.domain.Location;

import java.util.List;

public interface RouteCoordinatesService {
    List<List<Double>> fetchCoordinates(List<Location> locations);
}

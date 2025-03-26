package mk.ukim.finki.wayzi.service;

import mk.ukim.finki.wayzi.model.domain.Location;

import java.util.List;

public interface LocationService {
    Location findById(Long id);
    List<Location> findAllByDisplayNameContaining(String query);
}

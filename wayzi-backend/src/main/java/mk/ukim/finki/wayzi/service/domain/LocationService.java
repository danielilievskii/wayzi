package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.model.domain.Location;

import java.util.List;

public interface LocationService {
    List<Location> findAll();
    Location findById(Long id);
}

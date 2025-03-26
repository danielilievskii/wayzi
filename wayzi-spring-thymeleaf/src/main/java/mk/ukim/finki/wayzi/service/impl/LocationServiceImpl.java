package mk.ukim.finki.wayzi.service.impl;

import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.exception.LocationNotFoundException;
import mk.ukim.finki.wayzi.repository.LocationRepository;
import mk.ukim.finki.wayzi.service.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location findById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
    }

    @Override
    public List<Location> findAllByDisplayNameContaining(String query) {
        return locationRepository.findAllByDisplayNameContainingIgnoreCase(query);
    }
}

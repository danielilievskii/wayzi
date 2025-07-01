package mk.ukim.finki.wayzi.service.domain.impl;

import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.model.exception.ResourceNotFoundException;
import mk.ukim.finki.wayzi.repository.LocationRepository;
import mk.ukim.finki.wayzi.service.domain.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location findById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Location with id: %d was not found.", id)));
    }
}

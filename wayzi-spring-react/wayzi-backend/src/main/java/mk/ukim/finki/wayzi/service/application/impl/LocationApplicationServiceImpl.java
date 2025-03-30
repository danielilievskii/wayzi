package mk.ukim.finki.wayzi.service.application.impl;

import mk.ukim.finki.wayzi.dto.DisplayLocationDto;
import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.service.application.LocationApplicationService;
import mk.ukim.finki.wayzi.service.domain.LocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationApplicationServiceImpl implements LocationApplicationService {

    private final LocationService locationService;

    public LocationApplicationServiceImpl(LocationService locationService) {
        this.locationService = locationService;
    }

    @Override
    public List<DisplayLocationDto> findAll() {
        return DisplayLocationDto.from(locationService.findAll());
    }
}

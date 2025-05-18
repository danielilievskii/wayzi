package mk.ukim.finki.wayzi.service.application.impl;

import mk.ukim.finki.wayzi.service.application.RideStatusApplicationService;
import mk.ukim.finki.wayzi.service.domain.RideStatusService;
import mk.ukim.finki.wayzi.web.dto.ride.DisplayRideDto;
import mk.ukim.finki.wayzi.web.dto.ride.UpdateRideStatusDto;
import org.springframework.stereotype.Service;

@Service
public class RideStatusApplicationServiceImpl implements RideStatusApplicationService {

    private final RideStatusService rideStatusService;

    public RideStatusApplicationServiceImpl(RideStatusService rideStatusService) {
        this.rideStatusService = rideStatusService;
    }

    @Override
    public DisplayRideDto updateRideStatus(UpdateRideStatusDto updateRideStatusDto) {
        return DisplayRideDto.from(rideStatusService.transitionTo(
                updateRideStatusDto.id(), updateRideStatusDto.newStatus())
        );
    }
}

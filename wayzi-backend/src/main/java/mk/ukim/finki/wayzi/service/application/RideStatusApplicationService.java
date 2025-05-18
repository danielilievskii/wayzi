package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.web.dto.ride.DisplayRideDto;
import mk.ukim.finki.wayzi.web.dto.ride.UpdateRideStatusDto;

public interface RideStatusApplicationService {
    public DisplayRideDto updateRideStatus(UpdateRideStatusDto updateRideStatusDto);
}

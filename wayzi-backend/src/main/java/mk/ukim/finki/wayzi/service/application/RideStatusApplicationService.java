package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.web.dto.DisplayRideDto;
import mk.ukim.finki.wayzi.web.dto.UpdateRideStatusDto;

public interface RideStatusApplicationService {
    public DisplayRideDto updateRideStatus(UpdateRideStatusDto updateRideStatusDto);
}

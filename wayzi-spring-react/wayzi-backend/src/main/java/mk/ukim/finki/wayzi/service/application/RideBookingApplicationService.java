package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.web.dto.CreateRideBookingDto;
import mk.ukim.finki.wayzi.web.dto.DisplayRideBookingDto;

public interface RideBookingApplicationService {
    DisplayRideBookingDto bookRide(Long rideId, CreateRideBookingDto createRideBookingDto);

    void cancelRideBooking(Long rideBookingId);
}

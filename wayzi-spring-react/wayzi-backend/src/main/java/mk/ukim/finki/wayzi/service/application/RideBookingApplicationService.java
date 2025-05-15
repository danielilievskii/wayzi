package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.web.dto.ridebooking.*;

public interface RideBookingApplicationService {
    RideBookingDetailsDto getBookingDetailsForBooker(Long rideBookingId);
    RideBookingCheckInDto getBookingCheckInDetailsForDriver(Long rideBookingId);

    RideBookingPageDto findPageForUser(RideBookingFilterDto filterDto);

    RideBookingDetailsDto bookRide(Long rideId, CreateRideBookingDto createRideBookingDto);
    void cancelRideBooking(Long rideBookingId);
    RideBookingCheckInDto checkInPassenger(Long rideBookingId);
}

package mk.ukim.finki.wayzi.service.application.impl;

import mk.ukim.finki.wayzi.service.application.RideBookingApplicationService;
import mk.ukim.finki.wayzi.service.domain.RideBookingService;
import mk.ukim.finki.wayzi.web.dto.CreateRideBookingDto;
import mk.ukim.finki.wayzi.web.dto.DisplayRideBookingDto;
import org.springframework.stereotype.Service;

@Service
public class RideBookingApplicationServiceImpl implements RideBookingApplicationService {

    private final RideBookingService rideBookingService;

    public RideBookingApplicationServiceImpl(RideBookingService rideBookingService) {
        this.rideBookingService = rideBookingService;
    }

    @Override
    public DisplayRideBookingDto bookRide(Long rideId, CreateRideBookingDto createRideBookingDto) {
        return DisplayRideBookingDto.from(rideBookingService.bookRide(
                rideId,
                createRideBookingDto.paymentMethod(),
                createRideBookingDto.bookedSeats()
        ));
    }

    @Override
    public void cancelRideBooking(Long rideBookingId) {
        rideBookingService.cancelRideBooking(rideBookingId);
    }
}

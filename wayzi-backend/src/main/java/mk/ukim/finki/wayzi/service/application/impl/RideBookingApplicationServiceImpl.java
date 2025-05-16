package mk.ukim.finki.wayzi.service.application.impl;

import mk.ukim.finki.wayzi.service.application.RideBookingApplicationService;
import mk.ukim.finki.wayzi.service.domain.RideBookingService;
import mk.ukim.finki.wayzi.web.dto.ridebooking.*;
import org.springframework.stereotype.Service;

@Service
public class RideBookingApplicationServiceImpl implements RideBookingApplicationService {

    private final RideBookingService rideBookingService;

    public RideBookingApplicationServiceImpl(RideBookingService rideBookingService) {
        this.rideBookingService = rideBookingService;
    }
    @Override
    public RideBookingDetailsDto getBookingDetailsForBooker(Long rideBookingId) {
        return RideBookingDetailsDto.from(
                rideBookingService.findByIdEnsuringBookerOwnership(
                        rideBookingId
                )
        );
    }

    @Override
    public RideBookingCheckInDto getBookingCheckInDetailsForDriver(Long rideBookingId) {
        return RideBookingCheckInDto.from(
                rideBookingService.findByIdEnsuringDriverOwnership(
                        rideBookingId
                )
        );
    }

    @Override
    public RideBookingPageDto findPageForUser(RideBookingFilterDto filterDto) {
        return RideBookingPageDto.from(
                rideBookingService.findPageForUser(
                        filterDto.status(),
                        filterDto.pageNum(),
                        filterDto.pageSize()
                ));
    }

    @Override
    public RideBookingDetailsDto bookRide(Long rideId, CreateRideBookingDto createRideBookingDto) {
        return RideBookingDetailsDto.from(
                rideBookingService.bookRide(
                        rideId,
                        createRideBookingDto.paymentMethod(),
                        createRideBookingDto.bookedSeats(),
                        createRideBookingDto.message()
                ));
    }

    @Override
    public void cancelRideBooking(Long rideBookingId) {
        rideBookingService.cancelRideBooking(rideBookingId);
    }

    @Override
    public RideBookingCheckInDto checkInPassenger(Long rideBookingId) {
        return RideBookingCheckInDto.from(rideBookingService.checkInPassenger(rideBookingId));
    }
}

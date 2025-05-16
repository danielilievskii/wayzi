package mk.ukim.finki.wayzi.service.domain.impl;

import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.ride.RideBooking;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import mk.ukim.finki.wayzi.model.exception.InvalidRideStatusException;
import mk.ukim.finki.wayzi.repository.RideRepository;
import mk.ukim.finki.wayzi.service.domain.RideBookingService;
import mk.ukim.finki.wayzi.service.domain.RideService;
import mk.ukim.finki.wayzi.service.domain.RideStatusService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RideStatusServiceImpl implements RideStatusService {

    private final RideService rideService;
    private final RideBookingService rideBookingService;
    private final RideRepository rideRepository;

    public RideStatusServiceImpl(RideService rideService, RideBookingService rideBookingService, RideRepository rideRepository) {
        this.rideService = rideService;
        this.rideBookingService = rideBookingService;
        this.rideRepository = rideRepository;
    }

    private boolean canTransitionTo(RideStatus currentStatus, RideStatus newStatus) {
            return switch (newStatus) {
                case CONFIRMED -> currentStatus == RideStatus.PENDING;
                case STARTED -> currentStatus == RideStatus.CONFIRMED;
                case FINISHED -> currentStatus == RideStatus.STARTED;
                case CANCELLED -> currentStatus == RideStatus.PENDING ||  currentStatus ==  RideStatus.CONFIRMED;
                default -> false;
            };
    }

    private void validateRideCanStart(Ride ride) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isBefore(ride.getDepartureTime())) {
            throw new InvalidRideStatusException("The ride cannot start before the scheduled departure time.");
        }
    }

    @Override
    public Ride transitionTo(Long id, RideStatus newStatus) {
        Ride ride = rideService.findByIdAndCheckOwnership(id);
        RideStatus currentStatus = ride.getStatus();

        if(!canTransitionTo(currentStatus, newStatus)) {
            throw new InvalidRideStatusException("Transition from " + currentStatus.name() + " to " + newStatus.name() + " is not allowed.");
        }

        if (newStatus == RideStatus.CANCELLED) {
            updateRideBookings(ride, RideBookingStatus.CANCELLED);

            if(currentStatus == RideStatus.CONFIRMED) {
                // TODO: Penalties for the driver
            }

        } else if (newStatus == RideStatus.FINISHED) {
            updateRideBookings(ride, RideBookingStatus.ARCHIVED);
        }

        if (currentStatus == RideStatus.CONFIRMED && newStatus == RideStatus.STARTED) {
            validateRideCanStart(ride);
        }

        //TODO: Notify passengers of status change (publish event)

        ride.setStatus(newStatus);
        return rideService.save(ride);
    }

    public void updateRideBookings(Ride ride, RideBookingStatus bookingStatus) {
        for(RideBooking rideBooking : ride.getRideBookings()) {
            if(rideBooking.getBookingStatus() == RideBookingStatus.CONFIRMED) {
                rideBooking.setBookingStatus(bookingStatus);
                rideBookingService.save(rideBooking);
            }
        }
    }
}

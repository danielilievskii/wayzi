package mk.ukim.finki.wayzi.service.domain.impl;

import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import mk.ukim.finki.wayzi.model.exception.InvalidRideStatusException;
import mk.ukim.finki.wayzi.repository.RideRepository;
import mk.ukim.finki.wayzi.service.domain.RideService;
import mk.ukim.finki.wayzi.service.domain.RideStatusService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RideStatusServiceImpl implements RideStatusService {

    private final RideService rideService;
    private final RideRepository rideRepository;

    public RideStatusServiceImpl(RideService rideService, RideRepository rideRepository) {
        this.rideService = rideService;
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

        if(!canTransitionTo(ride.getStatus(), newStatus)) {
            throw new InvalidRideStatusException("Transition from " + ride.getStatus().name() + " to " + newStatus.name() + " is not allowed.");
        }

        if (ride.getStatus().equals(RideStatus.CONFIRMED) && newStatus.equals(RideStatus.CANCELLED)) {
            // TODO: Penalties for the driver
        }

        if (ride.getStatus().equals(RideStatus.CONFIRMED) && newStatus.equals(RideStatus.STARTED)) {
            validateRideCanStart(ride);
        }

        //TODO: Notify passengers of status change (publish event)

        ride.setStatus(newStatus);
        return rideRepository.save(ride);
    }
}

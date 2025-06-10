package mk.ukim.finki.wayzi.service.domain.impl;

import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.model.domain.RideBooking;
import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import mk.ukim.finki.wayzi.model.exception.InvalidRideStatusException;
import mk.ukim.finki.wayzi.service.domain.NotificationService;
import mk.ukim.finki.wayzi.service.domain.RideBookingService;
import mk.ukim.finki.wayzi.service.domain.RideService;
import mk.ukim.finki.wayzi.service.domain.RideStatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RideStatusServiceImpl implements RideStatusService {

    private final RideService rideService;
    private final RideBookingService rideBookingService;
    private final NotificationService notificationService;

    public RideStatusServiceImpl(RideService rideService, RideBookingService rideBookingService, NotificationService notificationService) {
        this.rideService = rideService;
        this.rideBookingService = rideBookingService;
        this.notificationService = notificationService;
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

    @Override
    @Transactional
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

        ride.setStatus(newStatus);
        rideService.save(ride);

        notifyPassengersOfRideStatusChange(ride, newStatus);

        return ride;
    }

    private void validateRideCanStart(Ride ride) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isBefore(ride.getDepartureTime())) {
            throw new InvalidRideStatusException("The ride cannot start before the scheduled departure time.");
        }
    }

    private void updateRideBookings(Ride ride, RideBookingStatus bookingStatus) {
        for(RideBooking rideBooking : ride.getRideBookings()) {
            if(rideBooking.getBookingStatus() == RideBookingStatus.CONFIRMED) {
                rideBooking.setBookingStatus(bookingStatus);
                rideBookingService.save(rideBooking);
            }
        }
    }

    private void notifyPassengersOfRideStatusChange(Ride ride, RideStatus rideStatus) {
        for(RideBooking rideBooking : ride.getRideBookings()) {
            if(rideBooking.getBookingStatus() == RideBookingStatus.CONFIRMED) {
                switch (rideStatus) {
                    case STARTED:
                        notificationService.notifyPassengerOfRideStart(rideBooking);
                        break;
                    case FINISHED:
                        notificationService.notifyPassengerOfRideFinish(rideBooking);
                        break;
                    case CANCELLED:
                        notificationService.notifyPassengerOfRideCancellation(rideBooking);
                        break;
                    case CONFIRMED:
                        notificationService.notifyPassengerOfRideConfirmation(rideBooking);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}

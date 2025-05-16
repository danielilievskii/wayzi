package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;

public interface RideStatusService {
    Ride transitionTo(Long id, RideStatus status);
}

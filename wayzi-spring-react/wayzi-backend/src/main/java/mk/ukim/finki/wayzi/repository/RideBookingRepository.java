package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.ride.RideBooking;

import java.util.List;

public interface RideBookingRepository extends JpaSpecificationRepository<RideBooking, Long>{
    List<RideBooking> findAllByRideId(Long rideId);
    List<RideBooking> findAllByBookerId(Long bookerId);
}

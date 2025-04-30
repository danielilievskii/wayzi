package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.ride.RideBooking;
import mk.ukim.finki.wayzi.model.enumeration.PaymentMethod;

public interface RideBookingService {

    RideBooking save(RideBooking rideBooking);

    RideBooking bookRide(
            Long rideId,
            PaymentMethod paymentMethod,
            Integer bookedSeats
    );

    RideBooking findByIdAndCheckOwnership(Long rideBookingId);
    void cancelRideBooking(Long rideBookingId);
}

package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.model.domain.ride.RideBooking;
import mk.ukim.finki.wayzi.model.enumeration.PaymentMethod;
import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;
import org.springframework.data.domain.Page;

public interface RideBookingService {

    RideBooking save(RideBooking rideBooking);
    RideBooking bookRide(
            Long rideId,
            PaymentMethod paymentMethod,
            Integer bookedSeats,
            String message
    );
    RideBooking cancelRideBooking(Long rideBookingId);
    RideBooking checkInPassenger(Long rideBookingId);

    RideBooking findById(Long rideBookingId);
    RideBooking findByIdEnsuringBookerOwnership(Long rideBookingId);
    RideBooking findByIdEnsuringDriverOwnership(Long rideBookingId);

    Page<RideBooking> findPageForUser(RideBookingStatus status, Integer pageNum, Integer pageSize);

}

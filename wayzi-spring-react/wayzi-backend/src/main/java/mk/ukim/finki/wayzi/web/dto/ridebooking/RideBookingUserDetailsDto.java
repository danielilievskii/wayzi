package mk.ukim.finki.wayzi.web.dto.ridebooking;

import mk.ukim.finki.wayzi.model.domain.ride.RideBooking;
import mk.ukim.finki.wayzi.model.enumeration.CheckInStatus;
import mk.ukim.finki.wayzi.model.enumeration.PaymentMethod;
import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;
import mk.ukim.finki.wayzi.web.dto.DisplayLocationDto;

import java.time.LocalDateTime;
import java.util.List;

public record RideBookingUserDetailsDto(
        String bookerName,
        Long bookerId,
        Integer bookedSeats
) {
    public static RideBookingUserDetailsDto from (RideBooking rideBooking) {
        return new RideBookingUserDetailsDto(
                rideBooking.getBooker().getName(),
                rideBooking.getBooker().getId(),
                rideBooking.getBookedSeats()
        );
    }

    public static List<RideBookingUserDetailsDto> from (List<RideBooking> rideBookings) {
        return rideBookings.stream().map(RideBookingUserDetailsDto::from).toList();
    }
}

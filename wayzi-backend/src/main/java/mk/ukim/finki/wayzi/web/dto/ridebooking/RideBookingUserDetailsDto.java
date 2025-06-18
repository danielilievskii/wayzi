package mk.ukim.finki.wayzi.web.dto.ridebooking;

import mk.ukim.finki.wayzi.model.domain.RideBooking;
import mk.ukim.finki.wayzi.model.enumeration.CheckInStatus;
import mk.ukim.finki.wayzi.model.enumeration.PaymentMethod;
import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public record RideBookingUserDetailsDto(
        String bookerName,
        Long bookerId,
        Integer bookedSeats,
        String message,
        Integer totalPrice,
        LocalDateTime bookingTime,
        PaymentMethod paymentMethod,
        RideBookingStatus bookingStatus,
        CheckInStatus checkInStatus
) {
    public static RideBookingUserDetailsDto from (RideBooking rideBooking) {
        return new RideBookingUserDetailsDto(
                rideBooking.getBooker().getName(),
                rideBooking.getBooker().getId(),
                rideBooking.getBookedSeats(),
                rideBooking.getMessage(),
                rideBooking.getTotalPrice(),
                rideBooking.getBookingTime(),
                rideBooking.getPaymentMethod(),
                rideBooking.getBookingStatus(),
                rideBooking.getCheckInStatus()
        );
    }

    public static List<RideBookingUserDetailsDto> from (List<RideBooking> rideBookings) {
        return rideBookings.stream().map(RideBookingUserDetailsDto::from).toList();
    }
}

package mk.ukim.finki.wayzi.web.dto;

import mk.ukim.finki.wayzi.model.domain.ride.RideBooking;
import mk.ukim.finki.wayzi.model.enumeration.PaymentMethod;
import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public record DisplayRideBookingDto(
        Long rideBookingId,
        Long rideId,
        String bookerName,
        PaymentMethod paymentMethod,
        RideBookingStatus rideBookingStatus,
        Integer bookedSeats,
        Integer totalPrice,
        LocalDateTime bookingTime
) {
    public static DisplayRideBookingDto from (RideBooking rideBooking) {
        return new DisplayRideBookingDto(
                rideBooking.getId(),
                rideBooking.getRide().getId(),
                rideBooking.getBooker().getName(),
                rideBooking.getPaymentMethod(),
                rideBooking.getBookingStatus(),
                rideBooking.getBookedSeats(),
                rideBooking.getTotalPrice(),
                rideBooking.getBookingTime()
        );
    }

    public static List<DisplayRideBookingDto> from (List<RideBooking> rideBookings) {
        return rideBookings.stream().map(DisplayRideBookingDto::from).toList();
    }
}

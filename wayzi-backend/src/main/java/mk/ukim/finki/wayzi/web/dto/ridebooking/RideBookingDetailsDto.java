package mk.ukim.finki.wayzi.web.dto.ridebooking;

import mk.ukim.finki.wayzi.model.domain.RideBooking;
import mk.ukim.finki.wayzi.model.enumeration.CheckInStatus;
import mk.ukim.finki.wayzi.model.enumeration.PaymentMethod;
import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;
import mk.ukim.finki.wayzi.web.dto.location.DisplayLocationDto;

import java.time.LocalDateTime;
import java.util.List;

public record RideBookingDetailsDto(
        Long rideBookingId,
        Long rideId,

        String driverName,
        Long driverId,
        DisplayLocationDto departureLocation,
        LocalDateTime departureTime,
        DisplayLocationDto arrivalLocation,
        LocalDateTime arrivalTime,

        PaymentMethod paymentMethod,
        RideBookingStatus rideBookingStatus,
        CheckInStatus checkInStatus,
        Integer bookedSeats,
        Integer totalPrice,
        String message,
        String qrCodeUrl,
        LocalDateTime bookingTime
) {
    public static RideBookingDetailsDto from (RideBooking rideBooking) {
        return new RideBookingDetailsDto(
                rideBooking.getId(),
                rideBooking.getRide().getId(),
                rideBooking.getRide().getDriver().getName(),
                rideBooking.getRide().getDriver().getId(),
                DisplayLocationDto.from(rideBooking.getRide().getDepartureLocation()),
                rideBooking.getRide().getDepartureTime(),
                DisplayLocationDto.from(rideBooking.getRide().getArrivalLocation()),
                rideBooking.getRide().getArrivalTime(),
                rideBooking.getPaymentMethod(),
                rideBooking.getBookingStatus(),
                rideBooking.getCheckInStatus(),
                rideBooking.getBookedSeats(),
                rideBooking.getTotalPrice(),
                rideBooking.getMessage(),
                rideBooking.getQrCodeUrl(),
                rideBooking.getBookingTime()
        );
    }

    public static List<RideBookingDetailsDto> from (List<RideBooking> rideBookings) {
        return rideBookings.stream().map(RideBookingDetailsDto::from).toList();
    }
}

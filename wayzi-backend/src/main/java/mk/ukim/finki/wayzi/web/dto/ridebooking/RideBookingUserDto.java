package mk.ukim.finki.wayzi.web.dto.ridebooking;

import mk.ukim.finki.wayzi.model.domain.RideBooking;

import java.util.List;

public record RideBookingUserDto(
        String bookerName,
        Long bookerId,
        Integer bookedSeats
) {
    public static RideBookingUserDto from (RideBooking rideBooking) {
        return new RideBookingUserDto(
                rideBooking.getBooker().getName(),
                rideBooking.getBooker().getId(),
                rideBooking.getBookedSeats()
        );
    }

    public static List<RideBookingUserDto> from (List<RideBooking> rideBookings) {
        return rideBookings.stream().map(RideBookingUserDto::from).toList();
    }
}

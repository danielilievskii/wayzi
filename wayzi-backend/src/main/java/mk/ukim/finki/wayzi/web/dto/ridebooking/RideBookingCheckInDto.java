package mk.ukim.finki.wayzi.web.dto.ridebooking;

import mk.ukim.finki.wayzi.model.domain.ride.RideBooking;
import mk.ukim.finki.wayzi.model.enumeration.CheckInStatus;
import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;
import java.util.List;

public record RideBookingCheckInDto(
        Long rideBookingId,

        String bookerName,
        Long bookerId,

        RideBookingStatus rideBookingStatus,
        CheckInStatus checkInStatus,
        Integer bookedSeats

) {
    public static RideBookingCheckInDto from (RideBooking rideBooking) {
        return new RideBookingCheckInDto(
                rideBooking.getId(),
                rideBooking.getBooker().getName(),
                rideBooking.getBooker().getId(),
                rideBooking.getBookingStatus(),
                rideBooking.getCheckInStatus(),
                rideBooking.getBookedSeats()
        );
    }

    public static List<RideBookingCheckInDto> from (List<RideBooking> rideBookings) {
        return rideBookings.stream().map(RideBookingCheckInDto::from).toList();
    }
}

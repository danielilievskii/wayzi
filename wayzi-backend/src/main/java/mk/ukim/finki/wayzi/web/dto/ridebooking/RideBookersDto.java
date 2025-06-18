package mk.ukim.finki.wayzi.web.dto.ridebooking;

import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import mk.ukim.finki.wayzi.web.dto.location.DisplayLocationDto;

import java.util.List;

public record RideBookersDto(
        Long id,
        DisplayLocationDto departureLocation,
        DisplayLocationDto arrivalLocation,
        Integer availableSeats,
        Integer pricePerSeat,
        RideStatus status,
        List<RideBookingUserDetailsDto> bookers
) {
    public static RideBookersDto from(Ride ride) {
        return new RideBookersDto(
                ride.getId(),
                DisplayLocationDto.from(ride.getArrivalLocation()),
                DisplayLocationDto.from(ride.getDepartureLocation()),
                ride.getAvailableSeats(),
                ride.getPricePerSeat(),
                ride.getStatus(),
                RideBookingUserDetailsDto.from(ride.getRideBookings())
        );
    }

    public static List<RideBookersDto> from(List<Ride> rides) {
        return rides.stream().map(RideBookersDto::from).toList();
    }
}

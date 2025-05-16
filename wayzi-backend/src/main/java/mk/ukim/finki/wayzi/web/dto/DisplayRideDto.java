package mk.ukim.finki.wayzi.web.dto;

import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;

import java.time.LocalDateTime;
import java.util.List;

public record DisplayRideDto(
        Long id,
        String driverName,
        Long driverId,
        DisplayLocationDto departureLocation,
        LocalDateTime departureTime,
        DisplayLocationDto arrivalLocation,
        LocalDateTime arrivalTime,
        DisplayVehicleDto vehicle,
        Integer availableSeats,
        Integer pricePerSeat,
        RideStatus rideStatus,
        List<DisplayRideStopDto> rideStops
) {
    public static DisplayRideDto from(
            Ride ride
    ) {
        return new DisplayRideDto(
                ride.getId(),
                ride.getDriver().getName(),
                ride.getDriver().getId(),
                DisplayLocationDto.from(ride.getDepartureLocation()),
                ride.getDepartureTime(),
                DisplayLocationDto.from(ride.getArrivalLocation()),
                ride.getArrivalTime(),
                DisplayVehicleDto.from(ride.getVehicle()),
                ride.getAvailableSeats(),
                ride.getPricePerSeat(),
                ride.getStatus(),
                DisplayRideStopDto.from(ride.getRideStops())
        );
    }

    public static List<DisplayRideDto> from (List<Ride> rides) {
        return rides.stream().map(DisplayRideDto::from).toList();
    }
}

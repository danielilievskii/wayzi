package mk.ukim.finki.wayzi.web.dto.ride;

import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import mk.ukim.finki.wayzi.web.dto.location.DisplayLocationDto;
import mk.ukim.finki.wayzi.web.dto.vehicle.DisplayVehicleDto;
import mk.ukim.finki.wayzi.web.dto.ridebooking.RideBookingUserDetailsDto;

import java.time.LocalDateTime;
import java.util.List;

public record RideDetailsDto(
        Long id,
        String driverName,
        Long driverId,
        DisplayLocationDto departureLocation,
        String departureAddress,
        LocalDateTime departureTime,
        DisplayLocationDto arrivalLocation,
        String arrivalAddress,
        LocalDateTime arrivalTime,
        DisplayVehicleDto vehicle,
        Integer availableSeats,
        Integer pricePerSeat,
        RideStatus rideStatus,
        List<DisplayRideStopDto> rideStops,
        List<RideBookingUserDetailsDto> rideBookingUsers
) {
    public static RideDetailsDto from(
            Ride ride
    ) {
        return new RideDetailsDto(
                ride.getId(),
                ride.getDriver().getName(),
                ride.getDriver().getId(),
                DisplayLocationDto.from(ride.getDepartureLocation()),
                ride.getDepartureAddress(),
                ride.getDepartureTime(),
                DisplayLocationDto.from(ride.getArrivalLocation()),
                ride.getArrivalAddress(),
                ride.getArrivalTime(),
                DisplayVehicleDto.from(ride.getVehicle()),
                ride.getAvailableSeats(),
                ride.getPricePerSeat(),
                ride.getStatus(),
                DisplayRideStopDto.from(ride.getRideStops()),
                RideBookingUserDetailsDto.from(ride.getActiveRideBookings())
        );
    }

    public static List<RideDetailsDto> from (List<Ride> rides) {
        return rides.stream().map(RideDetailsDto::from).toList();
    }
}

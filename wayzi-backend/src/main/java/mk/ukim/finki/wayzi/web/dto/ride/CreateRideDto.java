package mk.ukim.finki.wayzi.web.dto.ride;

import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.model.domain.User;
import mk.ukim.finki.wayzi.model.domain.Vehicle;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

public record CreateRideDto(
        @NotNull(message = "Departure location is required")
        Long departureLocationId,

        @NotNull(message = "Departure time is required")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime departureTime,

        @NotNull(message = "Departure address is required")
        String departureAddress,

        @NotNull(message = "Arrival location is required")
        Long arrivalLocationId,

        @NotNull(message = "Arrival time is required")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime arrivalTime,

        @NotNull(message = "Arrival address is required")
        String arrivalAddress,

        @NotNull(message = "Vehicle selection is required")
        Long vehicleId,

        @NotNull(message = "Available seats are required")
        @Min(value = 1, message = "There must be at least 1 available seat")
        Integer availableSeats,

        @NotNull(message = "Price per seat is required")
        @Min(value = 0, message = "Price per seat cannot be negative")
        Integer pricePerSeat,

        @NotNull(message = "Ride stops cannot be null")
        @Valid
        List<CreateRideStopDto> rideStops
) {
    public Ride toEntity(Location departureLocation, Location arrivalLocation, User driver, Vehicle vehicle, RideStatus status) {
        return new Ride(
                departureLocation,
                this.departureAddress,
                this.departureTime,
                arrivalLocation,
                this.arrivalAddress,
                this.arrivalTime,
                driver,
                vehicle,
                this.availableSeats,
                this.pricePerSeat,
                status
        );
    }
}

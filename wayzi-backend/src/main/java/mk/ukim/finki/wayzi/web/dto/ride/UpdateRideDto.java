package mk.ukim.finki.wayzi.web.dto.ride;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateRideDto(
        @NotNull(message = "Departure location is required")
        Long departureLocationId,

        @NotNull(message = "Departure time is required")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime departureTime,

        @NotNull(message = "Arrival location is required")
        Long arrivalLocationId,

        @NotNull(message = "Arrival time is required")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime arrivalTime,

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
        List<UpdateRideStopDto> rideStops
) {}

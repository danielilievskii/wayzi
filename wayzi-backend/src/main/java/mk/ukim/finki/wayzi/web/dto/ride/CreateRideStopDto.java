package mk.ukim.finki.wayzi.web.dto.ride;

import jakarta.validation.constraints.NotNull;
import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.ride.RideStop;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record CreateRideStopDto(
        @NotNull(message = "Stop location is required")
        Long locationId,

        @NotNull(message = "Stop time is required")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime stopTime,

        int stopOrder
) {

    public RideStop toEntity(Ride ride, Location location) {
        return new RideStop(ride, location, stopTime, stopOrder);
    }
}

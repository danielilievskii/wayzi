package mk.ukim.finki.wayzi.web.dto.ride;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record UpdateRideStopDto(
        Long id,

        @NotNull(message = "Stop location is required")
        Long locationId,

        String stopAddress,

        @NotNull(message = "Stop time is required")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime stopTime,

        int stopOrder
) { }

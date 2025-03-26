package mk.ukim.finki.wayzi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayRideDto {

    private Long id;

    @NotNull(message = "Departure location is required")
    private Long departureLocationId;

    @NotNull
    private String departureLocationName;

    @NotNull(message = "Departure time is required")

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival location is required")
    private Long arrivalLocationId;

    @NotNull
    private String arrivalLocationName;

    @NotNull(message = "Arrival time is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Vehicle selection is required")
    private Long vehicleId;

    @NotNull(message = "Available seats are required")
    @Min(value = 1, message = "There must be at least 1 available seat")
    private Integer availableSeats;

    @NotNull(message = "Price per seat is required")
    @Min(value = 0, message = "Price per seat cannot be negative")
    private Integer pricePerSeat;

    @NotNull(message = "Ride stops cannot be null")
    @Valid
    private List<DisplayRideStopDto> rideStops = new ArrayList<>();
}

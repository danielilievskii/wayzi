package mk.ukim.finki.wayzi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayRideStopDto {

    private Long id;

    @NotNull(message = "Stop location is required")
    private Long locationId;

    private String locationName;

    @NotNull(message = "Stop time is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime stopTime;

    private int stopOrder;
}

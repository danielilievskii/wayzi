package mk.ukim.finki.wayzi.dto;

import jakarta.validation.constraints.NotNull;
import mk.ukim.finki.wayzi.model.domain.ride.RideStop;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record DisplayRideStopDto(
        Long id,
        DisplayLocationDto location,
        LocalDateTime stopTime,
        int stopOrder
) {
    public static DisplayRideStopDto from (RideStop rideStop) {
        return new DisplayRideStopDto(
                rideStop.getId(),
                DisplayLocationDto.from(rideStop.getLocation()),
                rideStop.getStopTime(),
                rideStop.getStopOrder()
        );
    }

    public static List<DisplayRideStopDto> from (List<RideStop> rideStops) {
        return rideStops.stream().map(DisplayRideStopDto::from).toList();
    }

}

package mk.ukim.finki.wayzi.web.dto.ride;

import mk.ukim.finki.wayzi.model.domain.RideStop;
import mk.ukim.finki.wayzi.web.dto.location.DisplayLocationDto;

import java.time.LocalDateTime;
import java.util.List;

public record DisplayRideStopDto(
        Long id,
        DisplayLocationDto location,
        String stopAddress,
        LocalDateTime stopTime,
        int stopOrder
) {
    public static DisplayRideStopDto from (RideStop rideStop) {
        return new DisplayRideStopDto(
                rideStop.getId(),
                DisplayLocationDto.from(rideStop.getLocation()),
                rideStop.getStopAddress(),
                rideStop.getStopTime(),
                rideStop.getStopOrder()
        );
    }

    public static List<DisplayRideStopDto> from (List<RideStop> rideStops) {
        return rideStops.stream().map(DisplayRideStopDto::from).toList();
    }

}

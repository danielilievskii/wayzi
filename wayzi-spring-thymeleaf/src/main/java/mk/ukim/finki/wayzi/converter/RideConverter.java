package mk.ukim.finki.wayzi.converter;

import mk.ukim.finki.wayzi.dto.CreateRideDto;
import mk.ukim.finki.wayzi.dto.CreateRideStopDto;
import mk.ukim.finki.wayzi.dto.DisplayRideDto;
import mk.ukim.finki.wayzi.dto.DisplayRideStopDto;
import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.ride.RideStop;
import mk.ukim.finki.wayzi.service.LocationService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RideConverter {

    private final LocationService locationService;

    public RideConverter(LocationService locationService) {
        this.locationService = locationService;
    }

    public static CreateRideDto toCreateRideDtoFrom(Ride ride) {
        return new CreateRideDto(
                ride.getDepartureLocation().getId(),
                ride.getDepartureLocation().getName(),
                ride.getDepartureTime(),
                ride.getArrivalLocation().getId(),
                ride.getArrivalLocation().getName(),
                ride.getArrivalTime(),
                ride.getVehicle().getId(),
                ride.getAvailableSeats(),
                ride.getPricePerSeat(),
                RideConverter.toCreateRideStopDtoListFrom(ride.getRideStops())
        );
    }

    public static List<CreateRideDto> toCreateRideDtoListFrom (List<Ride> rides) {
        return rides.stream().map(RideConverter::toCreateRideDtoFrom).collect(Collectors.toList());
    }

    public static DisplayRideDto toDisplayRideDtoFrom(Ride ride) {
        return new DisplayRideDto(
                ride.getId(),
                ride.getDepartureLocation().getId(),
                ride.getDepartureLocation().getName(),
                ride.getDepartureTime(),
                ride.getArrivalLocation().getId(),
                ride.getArrivalLocation().getName(),
                ride.getArrivalTime(),
                ride.getVehicle().getId(),
                ride.getAvailableSeats(),
                ride.getPricePerSeat(),
                RideConverter.toDisplayRideStopDtoListFrom(ride.getRideStops())
        );
    }


    public Ride toRideEntity(CreateRideDto createRideDto) {
        Location departureLocation = locationService.findById(createRideDto.getDepartureLocationId());
        Location arrivalLocation = locationService.findById(createRideDto.getArrivalLocationId());

        return new Ride(
                departureLocation,
                createRideDto.getDepartureTime(),
                arrivalLocation,
                createRideDto.getArrivalTime(),
                createRideDto.getAvailableSeats(),
                createRideDto.getPricePerSeat()
        );
    }

    public static CreateRideStopDto toCreateRideStopDtoFrom(RideStop rideStop) {
        return new CreateRideStopDto(
                rideStop.getLocation().getId(),
                rideStop.getLocation().getName(),
                rideStop.getStopTime(),
                rideStop.getStopOrder()
        );
    }

    public static List<CreateRideStopDto> toCreateRideStopDtoListFrom(List<RideStop> rideStops) {
        return rideStops.stream().map(RideConverter::toCreateRideStopDtoFrom).collect(Collectors.toList());
    }

    public static DisplayRideStopDto toDisplayRideStopDtoFrom(RideStop rideStop) {
        return new DisplayRideStopDto(
                rideStop.getId(),
                rideStop.getLocation().getId(),
                rideStop.getLocation().getName(),
                rideStop.getStopTime(),
                rideStop.getStopOrder()
        );
    }

    public static List<DisplayRideStopDto> toDisplayRideStopDtoListFrom(List<RideStop> rideStops) {
        return rideStops.stream().map(RideConverter::toDisplayRideStopDtoFrom).collect(Collectors.toList());
    }

    public RideStop toRideStopEntity(CreateRideStopDto createRideStopDto) {
        Location stopLocation = locationService.findById(createRideStopDto.getLocationId());

        return new RideStop(
                stopLocation,
                createRideStopDto.getStopTime(),
                createRideStopDto.getStopOrder()
        );
    }
}

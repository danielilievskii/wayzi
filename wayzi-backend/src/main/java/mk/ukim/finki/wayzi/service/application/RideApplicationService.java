package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.web.dto.ride.*;
import mk.ukim.finki.wayzi.web.dto.ridebooking.RideBookersDto;

import java.util.List;

public interface RideApplicationService {

    DisplayRideDto save(CreateRideDto createRideDto);

    DisplayRideDto edit(Long id, UpdateRideDto updateRideDto);

    List<DisplayRideDto> findAll();

    RidePageDto findPage(RideFilterDto rideFilterDto);

    RidePageDto findPublishedRidesPageForUser(PublishedRideFilterDto publishedRideFilterDto);

    RideBookersDto findBookersById(Long id);

    RideDetailsDto findById(Long id);
    List<List<Double>> findRouteCoordinatesById(Long id);

    DisplayRideDto findByIdAndCheckOwnership(Long id);

    List<DisplayRideDto> findAllByVehicleIdForUser(Long vehicleId);
}

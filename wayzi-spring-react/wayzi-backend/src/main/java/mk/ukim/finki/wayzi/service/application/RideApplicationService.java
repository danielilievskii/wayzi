package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.web.dto.*;

import java.util.List;

public interface RideApplicationService {

    DisplayRideDto save(CreateRideDto createRideDto);

    DisplayRideDto edit(Long id, UpdateRideDto updateRideDto);

    List<DisplayRideDto> findAll();

    RidePageDto findPage(RideFilterDto rideFilterDto);

    RidePageDto findCurrentUserPublishedRidesPage(RideFilterDto rideFilterDto);


    DisplayRideDto findById(Long id);

    DisplayRideDto findByIdAndCheckOwnership(Long id);

    List<DisplayRideDto> findAllForAuthenticatedUserByVehicleId(Long vehicleId);
}

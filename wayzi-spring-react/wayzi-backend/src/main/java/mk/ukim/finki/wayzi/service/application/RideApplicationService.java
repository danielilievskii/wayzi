package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.dto.*;

import java.util.List;

public interface RideApplicationService {

    DisplayRideDto save(CreateRideDto createRideDto);
    DisplayRideDto edit(Long id, UpdateRideDto updateRideDto);

    List<DisplayRideDto> findAll();
    RidePageDto findPage(RideFilterDto rideFilterDto);

    DisplayRideDto findById(Long id);
    DisplayRideDto findByIdAndCheckOwnership(Long id);
    List<DisplayRideDto> findAllForAuthenticatedUser();
    List<DisplayRideDto> findAllForAuthenticatedUserByVehicleId(Long vehicleId);

    void confirmRide(Long id);
    void cancelRide(Long id);
    void startRide(Long id);
}

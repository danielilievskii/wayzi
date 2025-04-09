package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.dto.CreateRideDto;
import mk.ukim.finki.wayzi.dto.DisplayRideDto;
import mk.ukim.finki.wayzi.dto.UpdateRideDto;
import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface RideApplicationService {

    DisplayRideDto save(CreateRideDto createRideDto);
    DisplayRideDto edit(Long id, UpdateRideDto updateRideDto);

    List<DisplayRideDto> findAll();

    DisplayRideDto findById(Long id);
    DisplayRideDto findByIdAndCheckOwnership(Long id);
    List<DisplayRideDto> findAllForAuthenticatedUser();
    List<DisplayRideDto> findAllForAuthenticatedUserByVehicleId(Long vehicleId);

    void confirmRide(Long id);
    void cancelRide(Long id);
    void startRide(Long id);
}

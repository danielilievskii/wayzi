package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.web.dto.CreateRideDto;
import mk.ukim.finki.wayzi.web.dto.UpdateRideDto;
import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface RideService {

    Ride save(CreateRideDto createRideDto);
    Ride edit(Long id, UpdateRideDto updateRideDto);

    List<Ride> findAll();
    Page<Ride> findPage(Long departureLocationId, Long arrivalLocationId, LocalDate date, Integer passengersNum, Integer pageNum, Integer pageSize);
    Page<Ride> findCurrentUserPublishedRidesPage(Long departureLocationId, Long arrivalLocationId, LocalDate date, Integer passengersNum, Integer pageNum, Integer pageSize);

    Ride findById(Long id);
    Ride findByIdAndCheckOwnership(Long id);
    List<Ride> findAllForAuthenticatedUserByVehicleId(Long vehicleId);
}

package mk.ukim.finki.wayzi.service;

import mk.ukim.finki.wayzi.dto.DisplayRideDto;
import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.dto.CreateRideDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface RideService {

    Ride save(CreateRideDto createRideDto);
    Ride edit(Long id, DisplayRideDto displayRideDto);

    List<Ride> findAll();
    Page<Ride> findPage(Long departureLocationId, Long arrivalLocationId, LocalDate date, Integer passengersNum, Integer pageNum, Integer pageSize);

    Ride findById(Long id);
    Ride findByIdAndCheckOwnership(Long id);
    List<Ride> findAllForAuthenticatedUser();
    List<Ride> findAllForAuthenticatedUserByVehicleId(Long vehicleId);

    void confirmRide(Long id);
    void cancelRide(Long id);
    void startRide(Long id);
}

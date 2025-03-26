package mk.ukim.finki.wayzi.service;

import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.dto.CreateVehicleDto;

import java.util.List;

public interface VehicleService {
    Vehicle findById(Long id);
    Vehicle findByIdAndCheckOwnership(Long id);

    List<Vehicle> findAllForAuthenticatedUser();
    Vehicle save(CreateVehicleDto createVehicleDto);
    Vehicle update(Long id, CreateVehicleDto createVehicleDto);
    void delete(Long id);

}

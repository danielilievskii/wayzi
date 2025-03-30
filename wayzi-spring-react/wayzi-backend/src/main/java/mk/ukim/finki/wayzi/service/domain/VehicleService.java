package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.dto.CreateVehicleDto;
import mk.ukim.finki.wayzi.dto.DisplayVehicleDto;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle findById(Long id);
    Vehicle findByIdAndCheckOwnership(Long id);

    List<Vehicle> findAllForAuthenticatedUser();
    Vehicle save(CreateVehicleDto vehicle);
    Vehicle update(Long id, CreateVehicleDto vehicle);
    void delete(Long id);
}

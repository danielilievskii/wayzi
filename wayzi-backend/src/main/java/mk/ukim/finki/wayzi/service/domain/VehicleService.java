package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.web.dto.vehicle.CreateVehicleDto;
import mk.ukim.finki.wayzi.model.domain.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle findById(Long id);
    Vehicle findByIdAndCheckOwnership(Long id);

    List<Vehicle> findAllForUser();
    Vehicle save(CreateVehicleDto vehicle);
    Vehicle update(Long id, CreateVehicleDto vehicle);
    void delete(Long id);
}

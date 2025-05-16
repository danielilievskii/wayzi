package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.web.dto.CreateVehicleDto;
import mk.ukim.finki.wayzi.web.dto.DisplayVehicleDto;

import java.util.List;

public interface VehicleApplicationService {
    DisplayVehicleDto findByIdAndCheckOwnership(Long id);
    List<DisplayVehicleDto> findAllForAuthenticatedUser();
    DisplayVehicleDto save(CreateVehicleDto vehicle);
    DisplayVehicleDto update(Long id, CreateVehicleDto vehicle);
    void delete(Long id);
}

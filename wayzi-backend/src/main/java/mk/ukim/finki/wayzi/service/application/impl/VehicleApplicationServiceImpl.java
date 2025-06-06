package mk.ukim.finki.wayzi.service.application.impl;

import mk.ukim.finki.wayzi.web.dto.vehicle.CreateVehicleDto;
import mk.ukim.finki.wayzi.web.dto.vehicle.DisplayVehicleDto;
import mk.ukim.finki.wayzi.service.application.VehicleApplicationService;
import mk.ukim.finki.wayzi.service.domain.VehicleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleApplicationServiceImpl implements VehicleApplicationService {

    private final VehicleService vehicleService;

    public VehicleApplicationServiceImpl(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Override
    public DisplayVehicleDto findByIdAndCheckOwnership(Long id) {
        return DisplayVehicleDto.from(vehicleService.findByIdAndCheckOwnership(id));
    }

    @Override
    public List<DisplayVehicleDto> findAllForUser() {
        return DisplayVehicleDto.from(vehicleService.findAllForUser());
    }

    @Override
    public DisplayVehicleDto save(CreateVehicleDto vehicle) {
        return DisplayVehicleDto.from(vehicleService.save(vehicle));
    }

    @Override
    public DisplayVehicleDto update(Long id, CreateVehicleDto vehicle) {
        return DisplayVehicleDto.from(vehicleService.update(id, vehicle));
    }

    @Override
    public void delete(Long id) {
        vehicleService.delete(id);
    }
}

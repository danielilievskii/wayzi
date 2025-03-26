package mk.ukim.finki.wayzi.converter;

import mk.ukim.finki.wayzi.dto.CreateVehicleDto;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.service.AuthService;
import mk.ukim.finki.wayzi.service.StandardUserService;
import org.springframework.stereotype.Component;

@Component
public class VehicleConverter {

    private final StandardUserService standardUserService;
    private final AuthService authService;

    public VehicleConverter(StandardUserService standardUserService, AuthService authService) {
        this.standardUserService = standardUserService;
        this.authService = authService;
    }

    public Vehicle toEntity(CreateVehicleDto createVehicleDto) {
        return new Vehicle(
                createVehicleDto.getBrand(),
                createVehicleDto.getModel(),
                createVehicleDto.getColor(),
                createVehicleDto.getType(),
                createVehicleDto.getCapacity()
        );
    }
}

package mk.ukim.finki.wayzi.web.dto.vehicle;

import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.model.enumeration.Color;
import mk.ukim.finki.wayzi.model.enumeration.VehicleType;

import java.util.List;

public record DisplayVehicleDto(
        Long id,
        String brand,
        String model,
        Color color,
        VehicleType type,
        Integer capacity
) {
    public static DisplayVehicleDto from(Vehicle vehicle) {
        return new DisplayVehicleDto(
                vehicle.getId(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getColor(),
                vehicle.getType(),
                vehicle.getCapacity()
        );
    }

    public static List<DisplayVehicleDto> from (List<Vehicle> vehicles) {
        return vehicles.stream().map(DisplayVehicleDto::from).toList();
    }
}

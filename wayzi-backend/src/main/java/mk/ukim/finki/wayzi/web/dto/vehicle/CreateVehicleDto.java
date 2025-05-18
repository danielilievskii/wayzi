package mk.ukim.finki.wayzi.web.dto.vehicle;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.model.enumeration.VehicleType;
import mk.ukim.finki.wayzi.model.enumeration.Color;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;

public record CreateVehicleDto (
        @NotEmpty(message = "Brand is required")
        @Size(min = 2, max = 20, message = "Brand name must be between 2 and 20 characters")
        String brand,

        @NotEmpty(message = "Model is required")
        @Size(min = 2, max = 20, message = "Model name must be between 2 and 20 characters")
        String model,

        @NotNull(message = "Color is required")
        Color color,

        @NotNull(message = "Vehicle type is required")
        VehicleType type,

        @NotNull(message = "Capacity is required")
        @Min(value = 1, message = "Capacity must be greater than 0")
        Integer capacity
) {
    public Vehicle toEntity(User owner) {
        return new Vehicle(
                this.brand,
                this.model,
                this.color,
                this.type,
                this.capacity,
                owner
        );
    }
}
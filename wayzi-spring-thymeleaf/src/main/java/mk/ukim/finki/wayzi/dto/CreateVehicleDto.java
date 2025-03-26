package mk.ukim.finki.wayzi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.enumeration.Color;
import mk.ukim.finki.wayzi.enumeration.VehicleType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleDto {
    @NotEmpty(message = "Brand is required")
    @Size(min = 2, max = 20, message = "Brand name must be between 2 and 20 characters")
    private String brand;

    @NotEmpty(message = "Model is required")
    @Size(min = 2, max = 20, message = "Model name must be between 2 and 20 characters")
    private String model;

    @NotNull(message = "Color is required")
    private Color color;

    @NotNull(message = "Vehicle type is required")
    private VehicleType type;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;
}

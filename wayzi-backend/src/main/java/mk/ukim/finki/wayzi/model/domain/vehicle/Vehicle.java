package mk.ukim.finki.wayzi.model.domain.vehicle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.model.enumeration.Color;
import mk.ukim.finki.wayzi.model.enumeration.VehicleType;

@Data
@NoArgsConstructor
@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String model;

    @Enumerated(value = EnumType.STRING)
    private Color color;

    @Enumerated(value = EnumType.STRING)
    private VehicleType type;

    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    public Vehicle(String brand, String model, Color color, VehicleType type, int capacity) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.type = type;
        this.capacity = capacity;
    }

    public Vehicle(String brand, String model, Color color, VehicleType type, int capacity, User user) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.type = type;
        this.capacity = capacity;
        this.owner = user;
    }

    public String getName() {
        return this.brand + " " + this.model;
    }
}

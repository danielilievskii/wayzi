package mk.ukim.finki.wayzi.model.domain.vehicle;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.enumeration.Color;
import mk.ukim.finki.wayzi.enumeration.VehicleType;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;

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
    private StandardUser owner;

    public Vehicle(String brand, String model, Color color, VehicleType type, int capacity) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.type = type;
        this.capacity = capacity;

    }

    public Vehicle(String brand, String model, Color color, VehicleType type, int capacity, StandardUser user) {
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

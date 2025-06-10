package mk.ukim.finki.wayzi.model.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class RideStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private String stopAddress;

    private LocalDateTime stopTime;

    private int stopOrder;

    public RideStop(Location location, LocalDateTime stopTime, int stopOrder) {
        this.location = location;
        this.stopTime = stopTime;
        this.stopOrder = stopOrder;
    }

    public RideStop(Ride ride, Location location, String stopAddress, LocalDateTime stopTime, int stopOrder) {
        this.ride = ride;
        this.location = location;
        this.stopAddress = stopAddress;
        this.stopTime = stopTime;
        this.stopOrder = stopOrder;
    }
}

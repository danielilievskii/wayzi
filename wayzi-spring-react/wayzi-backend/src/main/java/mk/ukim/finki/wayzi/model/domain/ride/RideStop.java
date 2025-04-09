package mk.ukim.finki.wayzi.model.domain.ride;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.model.domain.Location;

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

    private LocalDateTime stopTime;

    private int stopOrder;

    public RideStop(Location location, LocalDateTime stopTime, int stopOrder) {
        this.location = location;
        this.stopTime = stopTime;
        this.stopOrder = stopOrder;
    }

    public RideStop(Ride ride, Location location, LocalDateTime stopTime, int stopOrder) {
        this.ride = ride;
        this.location = location;
        this.stopTime = stopTime;
        this.stopOrder = stopOrder;
    }
}

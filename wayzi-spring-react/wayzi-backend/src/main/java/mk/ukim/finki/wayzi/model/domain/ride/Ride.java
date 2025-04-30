package mk.ukim.finki.wayzi.model.domain.ride;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Location departureLocation;

    private LocalDateTime departureTime;

    @ManyToOne
    private Location arrivalLocation;

    private LocalDateTime arrivalTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private StandardUser driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private Integer availableSeats;

    private int pricePerSeat;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @OneToMany(mappedBy = "ride", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RideStop> rideStops;

    @OneToMany(mappedBy = "ride", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RideBooking> rideBookings;

    public Ride(Location departureLocation,
                LocalDateTime departureTime,
                Location arrivalLocation,
                LocalDateTime arrivalTime,
                Integer availableSeats,
                int pricePerSeat) {
        this.departureLocation = departureLocation;
        this.departureTime = departureTime;
        this.arrivalLocation = arrivalLocation;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
        this.pricePerSeat = pricePerSeat;
        this.rideStops = new ArrayList<>();
    }

    public Ride(Location departureLocation,
                LocalDateTime departureTime,
                Location arrivalLocation,
                LocalDateTime arrivalTime,
                StandardUser driver,
                Vehicle vehicle,
                Integer availableSeats,
                int pricePerSeat,
                RideStatus status) {
        this.departureLocation = departureLocation;
        this.departureTime = departureTime;
        this.arrivalLocation = arrivalLocation;
        this.arrivalTime = arrivalTime;
        this.driver = driver;
        this.vehicle = vehicle;
        this.availableSeats = availableSeats;
        this.pricePerSeat = pricePerSeat;
        this.status = status;
        this.rideStops = new ArrayList<>();
    }
}


package mk.ukim.finki.wayzi.model.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.converter.RouteCoordinatesConverter;
import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;
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

    //TODO: embedded class
    @ManyToOne
    private Location departureLocation;

    private String departureAddress;

    private LocalDateTime departureTime;

    @ManyToOne
    private Location arrivalLocation;

    private String arrivalAddress;

    private LocalDateTime arrivalTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private User driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private Integer availableSeats;

    private int pricePerSeat;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @Lob
    @Convert(converter = RouteCoordinatesConverter.class)
    private List<List<Double>> routeCoordinates;

    @OneToMany(mappedBy = "ride", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RideStop> rideStops;

    @OneToMany(mappedBy = "ride", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RideBooking> rideBookings;

    public Ride(Location departureLocation,
                String departureAddress,
                LocalDateTime departureTime,
                Location arrivalLocation,
                String arrivalAddress,
                LocalDateTime arrivalTime,
                Integer availableSeats,
                int pricePerSeat) {
        this.departureLocation = departureLocation;
        this.departureAddress = departureAddress;
        this.departureTime = departureTime;
        this.arrivalLocation = arrivalLocation;
        this.arrivalAddress = arrivalAddress;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
        this.pricePerSeat = pricePerSeat;
        this.rideStops = new ArrayList<>();
    }

    public Ride(Location departureLocation,
                String departureAddress,
                LocalDateTime departureTime,
                Location arrivalLocation,
                String arrivalAddress,
                LocalDateTime arrivalTime,
                User driver,
                Vehicle vehicle,
                Integer availableSeats,
                int pricePerSeat,
                RideStatus status) {
        this.departureLocation = departureLocation;
        this.departureAddress = departureAddress;
        this.departureTime = departureTime;
        this.arrivalLocation = arrivalLocation;
        this.arrivalAddress = arrivalAddress;
        this.arrivalTime = arrivalTime;
        this.driver = driver;
        this.vehicle = vehicle;
        this.availableSeats = availableSeats;
        this.pricePerSeat = pricePerSeat;
        this.status = status;
        this.rideStops = new ArrayList<>();
    }

    public List<RideBooking> getActiveRideBookings() {
        if (this.rideBookings == null) return List.of();
        return this.rideBookings.stream()
                .filter(rideBooking -> !rideBooking.getBookingStatus().equals(RideBookingStatus.CANCELLED))
                .toList();
    }
}


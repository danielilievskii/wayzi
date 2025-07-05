package mk.ukim.finki.wayzi.unit;

import mk.ukim.finki.wayzi.model.domain.*;
import mk.ukim.finki.wayzi.model.enumeration.*;

import java.time.LocalDateTime;

public class TestDataFactory {


    public static User createUser(Long id, String email) {
        User user = new User(email, "12345", "Daniel", true);
        user.setId(id);
        return user;
    }

    public static Vehicle createVehicle(Long id, User owner) {
        Vehicle vehicle = new Vehicle("Audi", "A3", Color.BLACK, VehicleType.AUTOMOBILE, 4, owner);
        vehicle.setId(id);
        return vehicle;
    }

    public static Location createLocation(Long id) {
        Location location = new Location();
        location.setId(id);
        return location;
    }

    public static Ride createRide(Long id, User user, Vehicle vehicle, Location dep, Location arr, LocalDateTime now) {
        Ride ride = new Ride(
                dep, "Address 1", now.plusHours(1),
                arr, "Address 2", now.plusHours(4),
                user, vehicle, 3, 300, RideStatus.PENDING
        );
        ride.setId(id);
        return ride;
    }

    public static RideStop createRideStop(Long id, Ride ride, Location loc, LocalDateTime time, int order) {
        RideStop stop = new RideStop(ride, loc, "Address 3", time, order);
        stop.setId(id);
        return stop;
    }

    public static RideBooking createRideBooking(Long id, Ride ride, User user, int bookedSeats, LocalDateTime time) {
        RideBooking rideBooking = new RideBooking(ride, user, PaymentMethod.CASH, RideBookingStatus.CONFIRMED, CheckInStatus.NOT_CHECKED_IN, 1, ride.getPricePerSeat()*bookedSeats, "qrcode", time);
        rideBooking.setId(id);
        return rideBooking;
    }
}

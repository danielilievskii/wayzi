package mk.ukim.finki.wayzi.enumeration;

public enum RideBookingStatus {
    PENDING,    // Booking request is waiting for driver approval
    CONFIRMED,  // Booking is accepted, passenger has a spot in the ride
    REJECTED,   // Driver rejected this passenger's booking request
    CANCELLED   // Passenger cancelled their booking before the ride started
}

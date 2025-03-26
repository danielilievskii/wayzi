package mk.ukim.finki.wayzi.enumeration;

public enum RideStatus {
    PENDING,
    CONFIRMED,  // Ride is scheduled and open for passengers to join
    STARTED,    // Ride has started
    FINISHED,   // Ride is completed
    CANCELLED   // Ride was cancelled
}

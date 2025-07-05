package mk.ukim.finki.wayzi.integration.base;

import mk.ukim.finki.wayzi.model.domain.*;
import mk.ukim.finki.wayzi.model.enumeration.*;
import mk.ukim.finki.wayzi.repository.*;
import mk.ukim.finki.wayzi.service.domain.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class AbstractIntegrationTestSetup {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected AdminRepository adminRepository;
    @Autowired
    protected VehicleRepository vehicleRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    protected RideRepository rideRepository;
    @Autowired
    protected RideBookingRepository rideBookingRepository;
    @Autowired
    protected JwtService jwtService;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected User user1;
    protected Admin user2;
    protected Vehicle vehicle1;
    protected Vehicle vehicle2;
    protected Location skopje;
    protected Location ohrid;
    protected Location bitola;
    protected Ride ride1;
    protected Ride ride2;
    protected Ride ride3;
    protected RideBooking rideBooking1;
    protected RideBooking rideBooking2;
    protected String tokenUser1;

    protected void initData() {
        rideBookingRepository.deleteAll();
        rideRepository.deleteAll();
        vehicleRepository.deleteAll();
        locationRepository.deleteAll();
        adminRepository.deleteAll();
        userRepository.deleteAll();

        // Users
        user1 = new User("daniel@gmail.com", passwordEncoder.encode("12345#*lozinka"), "Daniel", true);
        user1.setProfilePic("profile pic".getBytes());
        userRepository.save(user1);
        tokenUser1 = jwtService.generateToken(user1);

        user2 = new Admin("admin@gmail.com", passwordEncoder.encode("12345"), "Admin");
        adminRepository.save(user2);

        // Vehicles
        vehicle1 = new Vehicle("Audi", "A3", Color.BLACK, VehicleType.AUTOMOBILE, 4);
        vehicle1.setOwner(user1);
        vehicleRepository.save(vehicle1);

        vehicle2 = new Vehicle("BMW", "X5", Color.BLUE, VehicleType.AUTOMOBILE, 5);
        vehicle2.setOwner(user2);
        vehicleRepository.save(vehicle2);

        // Locations
        skopje = new Location();
        skopje.setCity("Skopje");
        locationRepository.save(skopje);

        ohrid = new Location();
        ohrid.setCity("Ohrid");
        locationRepository.save(ohrid);

        bitola = new Location();
        bitola.setCity("Bitola");
        locationRepository.save(bitola);

        // Rides
        ride1 = new Ride(
                skopje, "Address 1", LocalDateTime.now().plusHours(2),
                ohrid, "Address 2", LocalDateTime.now().plusHours(5),
                user1, vehicle1, 3, 200, RideStatus.CONFIRMED
        );
        rideRepository.save(ride1);

        ride2 = new Ride(
                bitola, "Address 1", LocalDateTime.now().plusDays(1),
                ohrid, "Address 2", LocalDateTime.now().plusDays(1).plusHours(2),
                user2, vehicle2, 2, 300, RideStatus.CONFIRMED
        );
        rideRepository.save(ride2);

        ride3 = new Ride(
                skopje, "Address 1", LocalDateTime.now().plusDays(2),
                bitola, "Address 2", LocalDateTime.now().plusDays(2).plusHours(3),
                user1, vehicle1, 1, 150, RideStatus.PENDING
        );
        rideRepository.save(ride3);

        // Ride Bookings
        rideBooking1 = new RideBooking(
                ride1, user2, PaymentMethod.CASH, RideBookingStatus.CONFIRMED,
                CheckInStatus.NOT_CHECKED_IN, 1, 200, "qrCode", LocalDateTime.now()
        );
        rideBookingRepository.save(rideBooking1);

        rideBooking2 = new RideBooking(
                ride2, user1, PaymentMethod.CASH, RideBookingStatus.CONFIRMED,
                CheckInStatus.NOT_CHECKED_IN, 1, 200, "qrCode", LocalDateTime.now()
        );
        rideBookingRepository.save(rideBooking2);
    }
}

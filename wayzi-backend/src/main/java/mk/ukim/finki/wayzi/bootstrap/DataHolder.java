package mk.ukim.finki.wayzi.bootstrap;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.ride.RideBooking;
import mk.ukim.finki.wayzi.model.domain.ride.RideStop;
import mk.ukim.finki.wayzi.model.domain.user.AdminUser;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.model.enumeration.*;
import mk.ukim.finki.wayzi.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {
    public static List<AdminUser> adminUsers = null;
    public static List<StandardUser> standardUsers = null;
    public static List<Vehicle> vehicles = null;
    public static List<Location> locations = null;
    public static List<Ride> rides = null;
    public static List<RideStop> rideStops = null;
    public static List<RideBooking> rideBookings = null;

    private final AdminUserRepository adminUserRepository;
    private final StandardUserRepository standardUserRepository;
    private final VehicleRepository vehicleRepository;
    private final LocationRepository locationRepository;
    private final PasswordEncoder passwordEncoder;
    private final RideRepository rideRepository;
    private final RideStopRepository rideStopRepository;
    private final RideBookingRepository rideBookingRepository;

    public DataHolder(AdminUserRepository adminUserRepository, StandardUserRepository standardUserRepository, VehicleRepository vehicleRepository, LocationRepository locationRepository, PasswordEncoder passwordEncoder, RideRepository rideRepository, RideStopRepository rideStopRepository, RideBookingRepository rideBookingRepository) {
        this.adminUserRepository = adminUserRepository;
        this.standardUserRepository = standardUserRepository;
        this.vehicleRepository = vehicleRepository;
        this.locationRepository = locationRepository;
        this.passwordEncoder = passwordEncoder;
        this.rideRepository = rideRepository;
        this.rideStopRepository = rideStopRepository;
        this.rideBookingRepository = rideBookingRepository;
    }

    @PostConstruct
    public void init() {
        standardUsers = new ArrayList<>();
        if(this.standardUserRepository.count() == 0) {
            standardUsers.add(new StandardUser("daniel@gmail.com", passwordEncoder.encode("12345"), "Daniel Ilievski"));
            standardUserRepository.saveAll(standardUsers);
        }

        adminUsers = new ArrayList<>();
        if(this.adminUserRepository.count() == 0) {
            adminUsers.add(new AdminUser("admin@admin.com", passwordEncoder.encode("admin"), "Admin"));
            adminUserRepository.saveAll(adminUsers);
        }

        vehicles = new ArrayList<>();
        if(this.vehicleRepository.count() == 0) {
            vehicles.add(new Vehicle("Volkswagen", "Golf", Color.BLACK, VehicleType.AUTOMOBILE,  5, standardUsers.get(0)));
            vehicles.add(new Vehicle("Audi", "A3", Color.BLUE, VehicleType.AUTOMOBILE, 3, standardUsers.get(0)));

            this.vehicleRepository.saveAll(vehicles);
        }
        locations = new ArrayList<>();
        if (this.locationRepository.count() == 0) {
            locations.add(new Location("Aracinovo", null, 42.0245, 21.5615));
            locations.add(new Location("Berovo", null, 41.7083, 22.8547));
            locations.add(new Location("Bitola", null, 41.0328, 21.3403));
            locations.add(new Location("Bogdanci", null, 41.2036, 22.5756));
            locations.add(new Location("Bogovinje", null, 41.9239, 20.9131));
            locations.add(new Location("Bosilovo", null, 41.4408, 22.7272));
            locations.add(new Location("Brvenica", null, 41.9678, 20.9806));
            locations.add(new Location("Valandovo", null, 41.3172, 22.5583));
            locations.add(new Location("Vasilevo", null, 41.4756, 22.7331));
            locations.add(new Location("Vevcani", null, 41.2400, 20.5919));
            locations.add(new Location("Veles", null, 41.7156, 21.7756));
            locations.add(new Location("Vinica", null, 41.8828, 22.5094));
//            locations.add(new Location("Vranestica", null));
//            locations.add(new Location("Vrapciste", null));
            locations.add(new Location("Gevgelija", null, 41.1417, 22.5014));
            locations.add(new Location("Gostivar", null, 41.8003, 20.9144));
            locations.add(new Location("Debar", null, 41.5242, 20.5281));
//            locations.add(new Location("Debarca", null));
            locations.add(new Location("Delcevo", null, 41.9675, 22.7697));
            locations.add(new Location("Demir Kapija", null, 41.4056, 22.2394));
//            locations.add(new Location("Demir Hisar", null));
//            locations.add(new Location("Dolneni", null));
//            locations.add(new Location("Drugovo", null));
//            locations.add(new Location("Zelino", null));
//            locations.add(new Location("Zajas", null));
//            locations.add(new Location("Zelenikovo", null));
//            locations.add(new Location("Zrnovci", null));
//            locations.add(new Location("Ilinden", null));
//            locations.add(new Location("Jegunovce", null));
            locations.add(new Location("Dojran", null, 41.1869, 22.7208));
//            locations.add(new Location("Karbinci", null));
            locations.add(new Location("Kavadarci", null, 41.4336, 22.0083));
            locations.add(new Location("Kicevo", null, 41.5172, 20.9522));
//            locations.add(new Location("Konche", null));
            locations.add(new Location("Kocani", null, 41.9161, 22.4128));
            locations.add(new Location("Kratovo", null, 42.0794, 22.1806));
            locations.add(new Location("Kriva Palanka", null, 42.2008, 22.3306));
//            locations.add(new Location("Krivogastani", null));
            locations.add(new Location("Krushevo", null, 41.3681, 21.2494));
            locations.add(new Location("Kumanovo", null, 42.1331, 21.7256));
//            locations.add(new Location("Lipkovo", null));
//            locations.add(new Location("Lozovo", null));
//            locations.add(new Location("Mavrovo i Rostuse", null));
//            locations.add(new Location("Makedonski Brod", null));
//            locations.add(new Location("Makedonska Kamenica", null));
//            locations.add(new Location("Mogila", null));
//            locations.add(new Location("Negotino", null));
//            locations.add(new Location("Novaci", null));
//            locations.add(new Location("Novo Selo", null));
//            locations.add(new Location("Oslomej", null));
            locations.add(new Location("Ohrid", null, 41.1214, 20.8019));
//            locations.add(new Location("Petrovec", null));
//            locations.add(new Location("Pehcevo", null));
//            locations.add(new Location("Plasnica", null));
            locations.add(new Location("Prilep", null, 41.3442, 21.5528));
            locations.add(new Location("Radovis", null, 41.6389, 22.4664));
            locations.add(new Location("Resen", null, 41.0883, 21.0103));
//            locations.add(new Location("Rosoman", null));
//            locations.add(new Location("Staro Nagoricane", null));
//            locations.add(new Location("Sveti Nikole", null));
//            locations.add(new Location("Sopiste", null));
            locations.add(new Location("Struga", null, 41.1778, 20.6786));
            locations.add(new Location("Strumica", null, 41.4378, 22.6428));
//            locations.add(new Location("Studenicani", null));
//            locations.add(new Location("Tearce", null));
            locations.add(new Location("Shtip", null, 41.7458, 22.1958));
            locations.add(new Location("Tetovo", null, 42.0086, 20.9714));
//            locations.add(new Location("Centar Zupa", null));
//            locations.add(new Location("Chashka", null));
//            locations.add(new Location("Cheshinovo i Obleshevo",null));

            locations.add(new Location("Aerodrom", "Skopje", 41.9833, 21.4667));
            locations.add(new Location("Butel", "Skopje", 42.0303, 21.4458));
            locations.add(new Location("Gazi Baba", "Skopje", 42.0008, 21.4803));
            locations.add(new Location("Gjorce Petrov", "Skopje", 42.0069, 21.3278));
            locations.add(new Location("Karpos", "Skopje", 42.0042, 21.3928));
            locations.add(new Location("Kisela Voda", "Skopje", 41.9856, 21.4339));
            locations.add(new Location("Saraj", "Skopje", 42.0131, 21.2789));
            locations.add(new Location("Centar", "Skopje", 41.9981, 21.4256));
            locations.add(new Location("Chair", "Skopje", 42.0156, 21.4411));
            locations.add(new Location("Shuto Orizari", "Skopje", 42.0400, 21.4250));
            locations.add(new Location("Skopje", null, 41.9981, 21.4256));

            this.locationRepository.saveAll(locations);
        }

        rides = new ArrayList<>();
        if(this.rideRepository.count() == 0) {
            rides.add(new Ride(locations.get(1), LocalDateTime.now(), locations.get(2), LocalDateTime.now().plusHours(4).plusMinutes(30), adminUsers.get(0), vehicles.get(0), 4, 200, RideStatus.CONFIRMED));
            rides.add(new Ride(locations.get(0), LocalDateTime.now(), locations.get(1), LocalDateTime.now().plusHours(4).plusMinutes(30), standardUsers.get(0), vehicles.get(0), 4, 200, RideStatus.STARTED));
            rides.add(new Ride(locations.get(14), LocalDateTime.now().plusHours(30), locations.get(21), LocalDateTime.now().plusHours(31).plusMinutes(30), standardUsers.get(0), vehicles.get(0), 4, 200, RideStatus.CONFIRMED));
            rides.add(new Ride(locations.get(15), LocalDateTime.now().plusHours(100), locations.get(30), LocalDateTime.now().plusHours(100).plusMinutes(30), standardUsers.get(0), vehicles.get(0), 4, 200, RideStatus.PENDING));
            rides.add(new Ride(locations.get(20), LocalDateTime.now().plusHours(1), locations.get(12), LocalDateTime.now().plusHours(4), standardUsers.get(0), vehicles.get(1), 2, 150, RideStatus.PENDING));
            this.rideRepository.saveAll(rides);
        }

        rideStops = new ArrayList<>();
        if(this.rideStopRepository.count() == 0) {
            rideStops.add(new RideStop(rides.get(0), locations.get(10), LocalDateTime.now().plusHours(1), 1));
            rideStops.add(new RideStop(rides.get(0), locations.get(31), LocalDateTime.now().plusHours(2), 2));
            this.rideStopRepository.saveAll(rideStops);
        }

        rideBookings = new ArrayList<>();
        if(this.rideBookingRepository.count() == 0) {
            rideBookings.add(new RideBooking(rides.get(0), standardUsers.get(0), PaymentMethod.CASH, RideBookingStatus.CONFIRMED, CheckInStatus.NOT_CHECKED_IN, 2, rides.get(0).getPricePerSeat() * 2, "test", LocalDateTime.now(), false, false));
            rideBookings.add(new RideBooking(rides.get(1), standardUsers.get(0), PaymentMethod.CASH, RideBookingStatus.ARCHIVED, CheckInStatus.NOT_CHECKED_IN, 2, rides.get(0).getPricePerSeat() * 2, "test", LocalDateTime.now(), false, false));
            rideBookings.add(new RideBooking(rides.get(2), standardUsers.get(0), PaymentMethod.CASH, RideBookingStatus.CANCELLED, CheckInStatus.NOT_CHECKED_IN, 2, rides.get(0).getPricePerSeat() * 2, "test", LocalDateTime.now(), false, false));
            this.rideBookingRepository.saveAll(rideBookings);
        }

    }
}

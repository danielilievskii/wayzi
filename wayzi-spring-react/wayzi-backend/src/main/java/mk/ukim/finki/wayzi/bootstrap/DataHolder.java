package mk.ukim.finki.wayzi.bootstrap;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wayzi.model.domain.user.AdminUser;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.model.enumeration.Color;
import mk.ukim.finki.wayzi.model.enumeration.VehicleType;
import mk.ukim.finki.wayzi.repository.AdminUserRepository;
import mk.ukim.finki.wayzi.repository.StandardUserRepository;
import mk.ukim.finki.wayzi.repository.VehicleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataHolder {
    public static List<AdminUser> adminUsers = null;
    public static List<StandardUser> standardUsers = null;
    public static List<Vehicle> vehicles = null;

    private final AdminUserRepository adminUserRepository;
    private final StandardUserRepository standardUserRepository;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataHolder(AdminUserRepository adminUserRepository, StandardUserRepository standardUserRepository, VehicleRepository vehicleRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.standardUserRepository = standardUserRepository;
        this.vehicleRepository = vehicleRepository;
        this.passwordEncoder = passwordEncoder;
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
    }
}

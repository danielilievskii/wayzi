package mk.ukim.finki.wayzi.model.domain.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.RideBooking;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.enumeration.Role;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "standardusers")
public class StandardUser extends User {

    public StandardUser(String email, String password, String name) {
        this.email = email;
        this.isEmailVerified = false;
        this.password = password;
        this.name = name;
        this.phoneNumber = "";
        this.isPhoneNumberVerified = false;
        this.profilePicPath = "uploads/profile-pics/default/user2.png";

        this.vehicles = new ArrayList<>();
        this.rides =  new ArrayList<>();
        this.rideBookings = new ArrayList<>();

        this.role = Role.ROLE_STANDARD_USER;
    }

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "phone_number_verified")
    private Boolean isPhoneNumberVerified;

    private String profilePicPath;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = false)
    List<Vehicle> vehicles;

    @OneToMany(mappedBy = "driver", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Ride> rides;

    @OneToMany(mappedBy = "rider", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<RideBooking> rideBookings;

}


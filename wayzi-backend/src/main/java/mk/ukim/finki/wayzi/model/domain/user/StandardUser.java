//package mk.ukim.finki.wayzi.model.domain.user;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import mk.ukim.finki.wayzi.model.enumeration.Role;
//import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Data
//@NoArgsConstructor
//@Table(name = "standard_user")
//public class StandardUser extends User {
//
//    public StandardUser(String email, String password, String name) {
//        this.email = email;
//        this.isEmailVerified = false;
//        this.password = password;
//        this.name = name;
//        this.phoneNumber = "";
//        this.isPhoneNumberVerified = false;
//        this.vehicles = new ArrayList<>();
//
//        this.role = Role.ROLE_STANDARD_USER;
//    }
//
//    @Column(name = "phone_number")
//    private String phoneNumber;
//
//    @Column(name = "phone_number_verified")
//    private Boolean isPhoneNumberVerified;
//
//    @Lob
//    @Column(name = "profile_pic", columnDefinition = "LONGBLOB")
//    private byte[] profilePic;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
//    List<Vehicle> vehicles;
//}

package mk.ukim.finki.wayzi.model.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.model.enumeration.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String email;

    @Column(name = "email_verified")
    protected Boolean isEmailVerified;

    @JsonIgnore
    protected String password;

    protected String name;

    @Enumerated(value = EnumType.STRING)
    protected Role role;

    @Column(name = "phone_number")
    protected String phoneNumber;

    @Column(name = "phone_number_verified")
    protected Boolean isPhoneNumberVerified;

    @Lob
    @Column(name = "profile_pic", columnDefinition = "LONGBLOB")
    protected byte[] profilePic;

    @JsonIgnore
    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)
    protected List<Vehicle> vehicles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.isEmailVerified = false;
        this.password = password;
        this.name = name;

        this.phoneNumber = "";
        this.isPhoneNumberVerified = false;
        this.vehicles = new ArrayList<>();

        this.role = Role.ROLE_STANDARD_USER;
    }
}

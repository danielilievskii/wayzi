package mk.ukim.finki.wayzi.model.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.enumeration.Role;

@Entity
@Data
@NoArgsConstructor
@Table(name = "standard_user")
public class StandardUser extends User {

    public StandardUser(String email, String password, String name) {
        this.email = email;
        this.isEmailVerified = false;
        this.password = password;
        this.name = name;

        this.phoneNumber = "";
        this.isPhoneNumberVerified = false;

        this.role = Role.ROLE_STANDARD_USER;
    }

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "phone_number_verified")
    private Boolean isPhoneNumberVerified;
}

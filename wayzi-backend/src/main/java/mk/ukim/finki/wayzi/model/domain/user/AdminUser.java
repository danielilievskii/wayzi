package mk.ukim.finki.wayzi.model.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.model.enumeration.Role;

@Entity
@Data
@NoArgsConstructor
@Table(name = "admin")
public class AdminUser extends StandardUser {

    public AdminUser(String email, String password, String name) {
        super(email, password, name);
        this.isEmailVerified = true;
        this.role = Role.ROLE_ADMIN_USER;
    }

}

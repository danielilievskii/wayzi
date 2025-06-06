package mk.ukim.finki.wayzi.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.model.enumeration.Role;

@Entity
@Data
@NoArgsConstructor
@Table(name = "admin")
public class Admin extends User {

    public Admin(String email, String password, String name) {
        super(email, password, name, true);
        this.role = Role.ROLE_ADMIN_USER;
    }

}

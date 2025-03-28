package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.user.AdminUser;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardUserRepository extends JpaRepository<StandardUser, Long> {
    StandardUser findByEmail(String email);

}

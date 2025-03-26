package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.user.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
}

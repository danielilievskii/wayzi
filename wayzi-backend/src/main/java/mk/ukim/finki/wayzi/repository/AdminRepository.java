package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}

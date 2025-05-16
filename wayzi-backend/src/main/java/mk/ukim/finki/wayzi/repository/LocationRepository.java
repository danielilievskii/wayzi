package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByName(String name);
    List<Location> findAllByDisplayNameContainingIgnoreCase(String query);
}

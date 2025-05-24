package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.RideStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideStopRepository extends JpaRepository<RideStop, Long> {

}

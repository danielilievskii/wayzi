package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.RideBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideBookingRepository extends JpaRepository<RideBooking, Long> {
}

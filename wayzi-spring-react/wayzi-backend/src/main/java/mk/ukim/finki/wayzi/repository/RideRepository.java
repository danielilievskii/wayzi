package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RideRepository extends JpaSpecificationRepository<Ride, Long>{
    List<Ride> findAllByDriverId(Long driverId);
    List<Ride> findAllByVehicleId(Long vehicleId);
    List<Ride> findAllByDriverIdAndVehicleId(Long driverId, Long vehicleId);
}

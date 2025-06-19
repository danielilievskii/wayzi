package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface RideRepository extends JpaSpecificationRepository<Ride, Long>{
    List<Ride> findAllByDriverId(Long driverId);
    List<Ride> findAllByVehicleId(Long vehicleId);
    List<Ride> findAllByDriverIdAndVehicleId(Long driverId, Long vehicleId);
    List<Ride> findByStatusAndDepartureTimeBefore(RideStatus status, LocalDateTime time);
    List<Ride> findByStatusAndArrivalTimeBefore(RideStatus status, LocalDateTime time);
}

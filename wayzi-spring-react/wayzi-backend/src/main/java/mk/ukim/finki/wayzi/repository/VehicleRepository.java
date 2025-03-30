package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findAllByOwnerId(Long userId);
}
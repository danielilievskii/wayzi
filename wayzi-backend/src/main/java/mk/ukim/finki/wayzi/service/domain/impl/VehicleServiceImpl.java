package mk.ukim.finki.wayzi.service.domain.impl;

import jakarta.transaction.Transactional;
import mk.ukim.finki.wayzi.web.dto.vehicle.CreateVehicleDto;
import mk.ukim.finki.wayzi.model.exception.AccessDeniedException;
import mk.ukim.finki.wayzi.model.exception.VehicleNotFoundException;
import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.model.domain.User;
import mk.ukim.finki.wayzi.model.domain.Vehicle;
import mk.ukim.finki.wayzi.repository.VehicleRepository;
import mk.ukim.finki.wayzi.service.domain.AuthService;
import mk.ukim.finki.wayzi.service.domain.RideService;
import mk.ukim.finki.wayzi.service.domain.VehicleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final AuthService authService;
    private final RideService rideService;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, AuthService authService, @Lazy RideService rideService) {
        this.vehicleRepository = vehicleRepository;
        this.authService = authService;
        this.rideService = rideService;
    }

    @Override
    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
    }

    @Override
    public Vehicle findByIdAndCheckOwnership(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));

        User currentUser = authService.getAuthenticatedUser();

        if(!vehicle.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException();
        }

        return vehicle;
    }

    @Override
    public List<Vehicle> findAllForUser() {
        Long userId = authService.getAuthenticatedUser().getId();
        return vehicleRepository.findAllByOwnerId(userId);
    }

    @Override
    public Vehicle save(CreateVehicleDto createVehicleDto) {
        User user = authService.getAuthenticatedUser();
        Vehicle vehicle = createVehicleDto.toEntity(user);

        return vehicleRepository.save(vehicle);
    }

    @Override
    @Transactional
    public Vehicle update(Long id, CreateVehicleDto createVehicleDto) {
        Vehicle vehicle = findByIdAndCheckOwnership(id);
        List<Ride> rides = rideService.findAllByVehicleIdForUser(id);

        if(rides.isEmpty()) {
            vehicle.setType(createVehicleDto.type());
            vehicle.setCapacity(createVehicleDto.capacity());
            vehicle.setModel(createVehicleDto.model());
            vehicle.setBrand(createVehicleDto.brand());
            vehicle.setColor(createVehicleDto.color());
            return vehicleRepository.save(vehicle);
        } else {
            vehicle.setOwner(null);
            vehicleRepository.save(vehicle);
            return save(createVehicleDto);
        }
    }

    @Override
    public void delete(Long id) {
        Vehicle vehicle = findByIdAndCheckOwnership(id);

        List<Ride> rides = rideService.findAllByVehicleIdForUser(id);
        if(rides.isEmpty()) {
            vehicleRepository.delete(vehicle);
        } else {
            vehicle.setOwner(null);
            vehicleRepository.save(vehicle);
        }
    }
}

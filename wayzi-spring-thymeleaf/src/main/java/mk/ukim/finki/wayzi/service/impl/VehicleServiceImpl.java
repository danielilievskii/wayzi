package mk.ukim.finki.wayzi.service.impl;

import mk.ukim.finki.wayzi.exception.AccessDeniedException;
import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.converter.VehicleConverter;
import mk.ukim.finki.wayzi.dto.CreateVehicleDto;
import mk.ukim.finki.wayzi.exception.VehicleNotFoundException;
import mk.ukim.finki.wayzi.repository.VehicleRepository;
import mk.ukim.finki.wayzi.service.AuthService;
import mk.ukim.finki.wayzi.service.RideService;
import mk.ukim.finki.wayzi.service.VehicleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final RideService rideService;
    private final VehicleConverter vehicleConverter;
    private final AuthService authService;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, @Lazy RideService rideService, VehicleConverter vehicleConverter, AuthService authService) {
        this.vehicleRepository = vehicleRepository;
        this.rideService = rideService;
        this.vehicleConverter = vehicleConverter;
        this.authService = authService;
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
    public List<Vehicle> findAllForAuthenticatedUser() {
        Long userId = authService.getAuthenticatedUserId();
        return vehicleRepository.findAllByOwnerId(userId);
    }

    @Override
    public Vehicle save(CreateVehicleDto createVehicleDto) {
        StandardUser user = authService.getAuthenticatedStandardUser();

        Vehicle vehicle = vehicleConverter.toEntity(createVehicleDto);
        vehicle.setOwner(user);

        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle update(Long id, CreateVehicleDto createVehicleDto) {
        Vehicle vehicle = findByIdAndCheckOwnership(id);
        List<Ride> rides = rideService.findAllForAuthenticatedUserByVehicleId(id);

        if(rides.isEmpty()) {
            vehicle.setType(createVehicleDto.getType());
            vehicle.setCapacity(createVehicleDto.getCapacity());
            vehicle.setModel(createVehicleDto.getModel());
            vehicle.setBrand(createVehicleDto.getBrand());
            vehicle.setColor(createVehicleDto.getColor());
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

        List<Ride> rides = rideService.findAllForAuthenticatedUserByVehicleId(id);
        if(rides.isEmpty()) {
            vehicleRepository.delete(vehicle);
        } else {
            vehicle.setOwner(null);
            vehicleRepository.save(vehicle);
        }
    }
}

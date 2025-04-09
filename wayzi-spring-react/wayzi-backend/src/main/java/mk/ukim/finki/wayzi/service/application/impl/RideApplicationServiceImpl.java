package mk.ukim.finki.wayzi.service.application.impl;

import mk.ukim.finki.wayzi.dto.CreateRideDto;
import mk.ukim.finki.wayzi.dto.DisplayRideDto;
import mk.ukim.finki.wayzi.dto.UpdateRideDto;
import mk.ukim.finki.wayzi.service.application.RideApplicationService;
import mk.ukim.finki.wayzi.service.domain.RideService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideApplicationServiceImpl implements RideApplicationService {

    private final RideService rideService;

    public RideApplicationServiceImpl(RideService rideService) {
        this.rideService = rideService;
    }

    @Override
    public DisplayRideDto save(CreateRideDto createRideDto) {
        return DisplayRideDto.from(
                rideService.save(createRideDto)
        );
    }

    @Override
    public DisplayRideDto edit(Long id, UpdateRideDto updateRideDto) {
        return DisplayRideDto.from(
                rideService.edit(id, updateRideDto)
        );
    }

    @Override
    public List<DisplayRideDto> findAll() {
        return DisplayRideDto.from(
                rideService.findAll()
        );
    }

    @Override
    public DisplayRideDto findById(Long id) {
        return DisplayRideDto.from(
                rideService.findById(id)
        );
    }

    @Override
    public DisplayRideDto findByIdAndCheckOwnership(Long id) {
        return DisplayRideDto.from(
                rideService.findByIdAndCheckOwnership(id)
        );
    }

    @Override
    public List<DisplayRideDto> findAllForAuthenticatedUser() {
        return DisplayRideDto.from(
                rideService.findAllForAuthenticatedUser()
        );
    }

    @Override
    public List<DisplayRideDto> findAllForAuthenticatedUserByVehicleId(Long vehicleId) {
        return DisplayRideDto.from(
                rideService.findAllForAuthenticatedUserByVehicleId(vehicleId)
        );
    }

    @Override
    public void confirmRide(Long id) {
        rideService.confirmRide(id);
    }

    @Override
    public void cancelRide(Long id) {
        rideService.cancelRide(id);
    }

    @Override
    public void startRide(Long id) {
        rideService.startRide(id);
    }
}

package mk.ukim.finki.wayzi.service.application.impl;

import jakarta.transaction.Transactional;
import mk.ukim.finki.wayzi.service.application.RideApplicationService;
import mk.ukim.finki.wayzi.service.domain.RideService;
import mk.ukim.finki.wayzi.web.dto.ride.*;
import mk.ukim.finki.wayzi.web.dto.ridebooking.RideBookersDto;
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
    public RidePageDto findPage(RideFilterDto rideFilterDto) {
        return RidePageDto.from(rideService.findPage(
                rideFilterDto.departureLocationId(),
                rideFilterDto.arrivalLocationId(),
                rideFilterDto.date(),
                rideFilterDto.passengersNum(),
                rideFilterDto.pageNum(),
                rideFilterDto.pageSize()
        ));
    }

    @Override
    public RidePageDto findPublishedRidesPageForUser(PublishedRideFilterDto publishedRideFilterDto) {
        return RidePageDto.from(rideService.findPublishedRidesPageForUser(
                publishedRideFilterDto.status(),
                publishedRideFilterDto.pageNum(),
                publishedRideFilterDto.pageSize()
        ));
    }

    @Override
    @Transactional
    public RideBookersDto findBookersById(Long id) {
        return RideBookersDto.from(
                rideService.findByIdAndCheckOwnership(id)
        );
    }

    @Override
    @Transactional
    public RideDetailsDto findById(Long id) {
        return RideDetailsDto.from(
                rideService.findById(id)
        );
    }

    @Override
    public List<List<Double>> findRouteCoordinatesById(Long id) {
        return rideService.findRouteCoordinatesById(id);
    }

    @Override
    public DisplayRideDto findByIdAndCheckOwnership(Long id) {
        return DisplayRideDto.from(
                rideService.findByIdAndCheckOwnership(id)
        );
    }

    @Override
    public List<DisplayRideDto> findAllByVehicleIdForUser(Long vehicleId) {
        return DisplayRideDto.from(
                rideService.findAllByVehicleIdForUser(vehicleId)
        );
    }

}

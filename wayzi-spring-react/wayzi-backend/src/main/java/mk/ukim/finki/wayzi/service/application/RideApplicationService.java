package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.web.dto.*;
import mk.ukim.finki.wayzi.web.dto.ride.PublishedRideFilterDto;
import mk.ukim.finki.wayzi.web.dto.ride.RideDetailsDto;
import mk.ukim.finki.wayzi.web.dto.ridebooking.RideBookingDetailsDto;

import java.util.List;

public interface RideApplicationService {

    DisplayRideDto save(CreateRideDto createRideDto);

    DisplayRideDto edit(Long id, UpdateRideDto updateRideDto);

    List<DisplayRideDto> findAll();

    RidePageDto findPage(RideFilterDto rideFilterDto);

    RidePageDto findPublishedRidesPageForUser(PublishedRideFilterDto publishedRideFilterDto);


    RideDetailsDto findById(Long id);

    DisplayRideDto findByIdAndCheckOwnership(Long id);

    List<DisplayRideDto> findAllForAuthenticatedUserByVehicleId(Long vehicleId);
}

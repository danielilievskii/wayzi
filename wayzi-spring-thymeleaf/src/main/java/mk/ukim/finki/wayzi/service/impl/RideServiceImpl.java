package mk.ukim.finki.wayzi.service.impl;

import mk.ukim.finki.wayzi.converter.RideConverter;
import mk.ukim.finki.wayzi.dto.CreateRideDto;
import mk.ukim.finki.wayzi.dto.DisplayRideDto;
import mk.ukim.finki.wayzi.dto.DisplayRideStopDto;
import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.model.domain.ride.*;
import mk.ukim.finki.wayzi.exception.AccessDeniedException;
import mk.ukim.finki.wayzi.enumeration.RideStatus;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.exception.InvalidRideStatusException;
import mk.ukim.finki.wayzi.exception.RideNotFoundException;
import mk.ukim.finki.wayzi.exception.RideStopNotFoundException;
import mk.ukim.finki.wayzi.repository.RideRepository;
import mk.ukim.finki.wayzi.repository.RideStopRepository;
import mk.ukim.finki.wayzi.service.AuthService;
import mk.ukim.finki.wayzi.service.LocationService;
import mk.ukim.finki.wayzi.service.RideService;
import mk.ukim.finki.wayzi.service.VehicleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static mk.ukim.finki.wayzi.service.specifications.FieldFilterSpecification.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideConverter rideConverter;
    private final AuthService authService;
    private final VehicleService vehicleService;
    private final LocationService locationService;
    private final RideStopRepository rideStopRepository;

    public RideServiceImpl(RideRepository rideRepository, RideConverter rideConverter, AuthService authService, @Lazy VehicleService vehicleService, LocationService locationService, RideStopRepository rideStopRepository) {
        this.rideRepository = rideRepository;
        this.rideConverter = rideConverter;
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.locationService = locationService;
        this.rideStopRepository = rideStopRepository;
    }

    @Override
    public Ride save(CreateRideDto createRideDto) {
        // Check if the user is authenticated (will be delegated to Spring Security)
        StandardUser authenticatedUser = authService.getAuthenticatedStandardUser();

        // Check if the user owns the vehicle
        Vehicle vehicle = vehicleService.findByIdAndCheckOwnership(createRideDto.getVehicleId());

        Ride ride = rideConverter.toRideEntity(createRideDto);
        ride.setStatus(RideStatus.PENDING);
        ride.setDriver(authenticatedUser);
        ride.setVehicle(vehicle);

        List<RideStop> rideStops = new ArrayList<>();
        createRideDto.getRideStops().forEach(stopDTO -> {
            RideStop stop = rideConverter.toRideStopEntity(stopDTO);
            stop.setRide(ride);

            rideStops.add(stop);
        });
        ride.setRideStops(rideStops);

        return rideRepository.save(ride);

    }

    @Override
    public Ride edit(Long id, DisplayRideDto displayRideDto) {
        Ride ride = findByIdAndCheckOwnership(id);

        Vehicle vehicle = vehicleService.findByIdAndCheckOwnership(displayRideDto.getVehicleId());
        Location departureLocation = locationService.findById(displayRideDto.getDepartureLocationId());
        Location arrivalLocation = locationService.findById(displayRideDto.getArrivalLocationId());

        ride.setDepartureLocation(departureLocation);
        ride.setDepartureTime(displayRideDto.getDepartureTime());
        ride.setArrivalLocation(arrivalLocation);
        ride.setArrivalTime(displayRideDto.getArrivalTime());
        ride.setAvailableSeats(displayRideDto.getAvailableSeats());
        ride.setPricePerSeat(displayRideDto.getPricePerSeat());
        ride.setVehicle(vehicle);


        Set<Long> rideStopIds = displayRideDto.getRideStops().stream()
                .map(DisplayRideStopDto::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Remove any ride stops that are no longer in the DTO (they are orphans)
        List<RideStop> rideStopsToRemove = new ArrayList<>();
        for (RideStop existingStop : ride.getRideStops()) {
            if (!rideStopIds.contains(existingStop.getId())) {
                rideStopsToRemove.add(existingStop);
            }
        }

        // Remove the orphaned ride stops from the collection
        ride.getRideStops().removeAll(rideStopsToRemove);

        for (DisplayRideStopDto displayRideStopDto : displayRideDto.getRideStops()) {

            RideStop rideStop;
            if (displayRideStopDto.getId() == null) {
                rideStop = new RideStop();
                rideStop.setRide(ride);
            } else {
                // TODO: findRideStopByIdAndCheckOwnership
                rideStop = rideStopRepository.findById(displayRideStopDto.getId())
                        .orElseThrow(() -> new RideStopNotFoundException(displayRideStopDto.getId()));
            }

            Location stopLocation = locationService.findById(displayRideStopDto.getLocationId());
            rideStop.setLocation(stopLocation);
            rideStop.setStopTime(displayRideStopDto.getStopTime());
            rideStop.setStopOrder(displayRideStopDto.getStopOrder());

            // If this is a new stop, add it to the list of ride stops
            if (!ride.getRideStops().contains(rideStop)) {
                ride.getRideStops().add(rideStop);
            }
        }
        return rideRepository.save(ride);
    }

    @Override
    public List<Ride> findAll() {
        return rideRepository.findAll();
    }

    @Override
    public Page<Ride> findPage(Long departureLocationId, Long arrivalLocationId, LocalDate date, Integer passengersNum, Integer pageNum, Integer pageSize) {


        Specification<Ride> specification = Specification
                .where(filterEquals(Ride.class, "departureLocation.id", departureLocationId))
                .and(filterEquals(Ride.class, "arrivalLocation.id", arrivalLocationId))
                .and(greaterThan(Ride.class,"availableSeats", passengersNum));

        if(date != null) {
            specification = specification.and(greaterThan(Ride.class, "departureTime", date.atStartOfDay()));
        }

        return this.rideRepository.findAll(
                specification,
                PageRequest.of(pageNum - 1, pageSize)
        );

    }


    @Override
    public Ride findById(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(id));
    }

    @Override
    public Ride findByIdAndCheckOwnership(Long id) {
        Ride ride = rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(id));

        User currentUser = authService.getAuthenticatedUser();

        if(!ride.getDriver().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException();
        }

        return ride;
    }

    @Override
    public List<Ride> findAllForAuthenticatedUser() {
        Long driverId = authService.getAuthenticatedUserId();
        return rideRepository.findAllByDriverId(driverId);
    }

    @Override
    public List<Ride> findAllForAuthenticatedUserByVehicleId(Long vehicleId) {
        Long driverId = authService.getAuthenticatedUserId();
        return rideRepository.findAllByDriverIdAndVehicleId(driverId, vehicleId);
    }

    @Override
    public void confirmRide(Long id) {
        Ride ride = findByIdAndCheckOwnership(id);

        if (!ride.getStatus().equals(RideStatus.PENDING)) {
            throw new InvalidRideStatusException("Only rides in the PENDING state can be confirmed.");
        }

        ride.setStatus(RideStatus.CONFIRMED);
        rideRepository.save(ride);

        // TODO: Notify passengers
    }

    @Override
    public void cancelRide(Long id) {
        Ride ride = findByIdAndCheckOwnership(id);

        if (ride.getStatus().equals(RideStatus.FINISHED)) {
            throw new InvalidRideStatusException("You can't cancel finished rides.");
        }

        if (!ride.getStatus().equals(RideStatus.CONFIRMED)) {
          // TODO: Penalties for the driver
        }

        ride.setStatus(RideStatus.CANCELLED);
        rideRepository.save(ride);

        // TODO: Notify passengers
    }

    @Override
    public void startRide(Long id) {
        Ride ride = findByIdAndCheckOwnership(id);

        if (!ride.getStatus().equals(RideStatus.CONFIRMED)) {
            throw new InvalidRideStatusException("The ride can only be started if it is in the CONFIRMED state.");
        }

        LocalDateTime currentTime = LocalDateTime.now();
        if(currentTime.isBefore(ride.getDepartureTime())) {
            throw new InvalidRideStatusException("The ride cannot start before the scheduled departure time.");
        }

        ride.setStatus(RideStatus.STARTED);
        rideRepository.save(ride);

        // TODO: Notify passengers
    }
}

package mk.ukim.finki.wayzi.service.domain.impl;

import mk.ukim.finki.wayzi.dto.CreateRideDto;
import mk.ukim.finki.wayzi.dto.UpdateRideDto;
import mk.ukim.finki.wayzi.dto.UpdateRideStopDto;
import mk.ukim.finki.wayzi.model.exception.AccessDeniedException;
import mk.ukim.finki.wayzi.model.exception.InvalidRideStatusException;
import mk.ukim.finki.wayzi.model.exception.RideNotFoundException;
import mk.ukim.finki.wayzi.model.exception.RideStopNotFoundException;
import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.ride.RideStop;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import mk.ukim.finki.wayzi.repository.RideRepository;
import mk.ukim.finki.wayzi.repository.RideStopRepository;
import mk.ukim.finki.wayzi.service.domain.AuthService;
import mk.ukim.finki.wayzi.service.domain.LocationService;
import mk.ukim.finki.wayzi.service.domain.RideService;
import mk.ukim.finki.wayzi.service.domain.VehicleService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static mk.ukim.finki.wayzi.specifications.FieldFilterSpecification.filterEquals;
import static mk.ukim.finki.wayzi.specifications.FieldFilterSpecification.greaterThan;

@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final AuthService authService;
    private final VehicleService vehicleService;
    private final LocationService locationService;
    private final RideStopRepository rideStopRepository;

    public RideServiceImpl(RideRepository rideRepository, AuthService authService, @Lazy VehicleService vehicleService, LocationService locationService, RideStopRepository rideStopRepository) {
        this.rideRepository = rideRepository;
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
        Vehicle vehicle = vehicleService.findByIdAndCheckOwnership(createRideDto.vehicleId());
        Location departureLocation = locationService.findById(createRideDto.departureLocationId());
        Location arrivalLocaiton = locationService.findById(createRideDto.arrivalLocationId());

        Ride ride = createRideDto.toEntity(departureLocation, arrivalLocaiton, authenticatedUser, vehicle, RideStatus.PENDING);

        List<RideStop> rideStops = new ArrayList<>();
        createRideDto.rideStops().forEach(stopDTO -> {
            Location location = locationService.findById(stopDTO.locationId());
            RideStop stop = stopDTO.toEntity(ride, location);

            rideStops.add(stop);
        });
        ride.setRideStops(rideStops);

        return rideRepository.save(ride);
    }

    @Override
    public Ride edit(Long id, UpdateRideDto updateRideDto) {
        Ride ride = findByIdAndCheckOwnership(id);

        Vehicle vehicle = vehicleService.findByIdAndCheckOwnership(updateRideDto.vehicleId());
        Location departureLocation = locationService.findById(updateRideDto.departureLocationId());
        Location arrivalLocation = locationService.findById(updateRideDto.arrivalLocationId());

        ride.setDepartureLocation(departureLocation);
        ride.setDepartureTime(updateRideDto.departureTime());
        ride.setArrivalLocation(arrivalLocation);
        ride.setArrivalTime(updateRideDto.arrivalTime());
        ride.setAvailableSeats(updateRideDto.availableSeats());
        ride.setPricePerSeat(updateRideDto.pricePerSeat());
        ride.setVehicle(vehicle);


        Set<Long> rideStopIds = updateRideDto.rideStops().stream()
                .map(UpdateRideStopDto::id)
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

        for (UpdateRideStopDto updateRideStopDto : updateRideDto.rideStops()) {

            RideStop rideStop;
            if (updateRideStopDto.id() == null) {
                rideStop = new RideStop();
                rideStop.setRide(ride);
            } else {
                // TODO: findRideStopByIdAndCheckOwnership
                rideStop = rideStopRepository.findById(updateRideStopDto.id())
                        .orElseThrow(() -> new RideStopNotFoundException(updateRideStopDto.id()));
            }

            Location stopLocation = locationService.findById(updateRideStopDto.locationId());
            rideStop.setLocation(stopLocation);
            rideStop.setStopTime(updateRideStopDto.stopTime());
            rideStop.setStopOrder(updateRideStopDto.stopOrder());

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
                PageRequest.of(pageNum - 1, pageSize, Sort.by("departureTime").descending())
        );
    }

    @Override
    public Ride findById(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RideStopNotFoundException(id));
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
        Long driverId = authService.getAuthenticatedUser().getId();
        return rideRepository.findAllByDriverId(driverId);
    }

    @Override
    public List<Ride> findAllForAuthenticatedUserByVehicleId(Long vehicleId) {
        Long driverId = authService.getAuthenticatedUser().getId();
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

        if (ride.getStatus().equals(RideStatus.STARTED)) {
            throw new InvalidRideStatusException("You can't cancel started rides.");
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

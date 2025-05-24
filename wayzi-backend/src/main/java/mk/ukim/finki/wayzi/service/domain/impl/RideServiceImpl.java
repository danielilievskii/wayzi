package mk.ukim.finki.wayzi.service.domain.impl;

import mk.ukim.finki.wayzi.service.domain.*;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideDto;
import mk.ukim.finki.wayzi.web.dto.ride.UpdateRideDto;
import mk.ukim.finki.wayzi.web.dto.ride.UpdateRideStopDto;
import mk.ukim.finki.wayzi.model.exception.AccessDeniedException;
import mk.ukim.finki.wayzi.model.exception.RideNotFoundException;
import mk.ukim.finki.wayzi.model.exception.RideStopNotFoundException;
import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.model.domain.RideStop;
import mk.ukim.finki.wayzi.model.domain.User;
import mk.ukim.finki.wayzi.model.domain.Vehicle;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import mk.ukim.finki.wayzi.repository.RideRepository;
import mk.ukim.finki.wayzi.repository.RideStopRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static mk.ukim.finki.wayzi.specifications.FieldFilterSpecification.*;

@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final AuthService authService;
    private final VehicleService vehicleService;
    private final LocationService locationService;
    private final RideStopRepository rideStopRepository;
    private final RouteCoordinatesService routeCoordinatesService;

    public RideServiceImpl(RideRepository rideRepository, AuthService authService, @Lazy VehicleService vehicleService, LocationService locationService, RideStopRepository rideStopRepository, RouteCoordinatesService routeCoordinatesService) {
        this.rideRepository = rideRepository;
        this.authService = authService;
        this.vehicleService = vehicleService;
        this.locationService = locationService;
        this.rideStopRepository = rideStopRepository;
        this.routeCoordinatesService = routeCoordinatesService;
    }

    @Override
    public Ride save(Ride ride) {
        return rideRepository.save(ride);
    }

    @Override
    public Ride save(CreateRideDto createRideDto) {
        User user = authService.getAuthenticatedUser();

        // Check if the user owns the vehicle
        Vehicle vehicle = vehicleService.findByIdAndCheckOwnership(createRideDto.vehicleId());

        Location departureLocation = locationService.findById(createRideDto.departureLocationId());
        Location arrivalLocaiton = locationService.findById(createRideDto.arrivalLocationId());

        Ride ride = createRideDto.toEntity(departureLocation, arrivalLocaiton, user, vehicle, RideStatus.PENDING);

        List<Location> locations = new ArrayList<>();
        locations.add(departureLocation);

        List<RideStop> rideStops = buildRideStops(createRideDto, ride, locations);
        ride.setRideStops(rideStops);

        locations.add(arrivalLocaiton);

        List<List<Double>> routeCoordinates = routeCoordinatesService.fetchCoordinates(locations);
        ride.setRouteCoordinates(routeCoordinates);

        return rideRepository.save(ride);
    }

    private List<RideStop> buildRideStops(CreateRideDto dto, Ride ride, List<Location> locations) {
        return dto.rideStops().stream()
                .map(stopDTO -> {
                    Location location = locationService.findById(stopDTO.locationId());
                    locations.add(location);

                    return stopDTO.toEntity(ride, location);
                })
                .collect(Collectors.toList());
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
                PageRequest.of(pageNum - 1, pageSize, Sort.by("departureTime").ascending())
        );
    }

    @Override
    public Page<Ride> findPublishedRidesPageForUser(RideStatus status, Integer pageNum, Integer pageSize) {
        Long driverId = authService.getAuthenticatedUser().getId();

        Specification<Ride> specification = Specification
                .where(filterEquals(Ride.class, "driver.id", driverId));

        if (status != null) {
            specification = specification.and(filterEqualsV(Ride.class, "status", status));
        }

        return this.rideRepository.findAll(
                specification,
                PageRequest.of(pageNum - 1, pageSize, Sort.by("departureTime").ascending())
        );
    }

    @Override
    public Ride findById(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RideStopNotFoundException(id));
    }

    @Override
    public List<List<Double>> findRouteCoordinatesById(Long id) {
        return findById(id).getRouteCoordinates();
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
    public List<Ride> findAllByVehicleIdForUser(Long vehicleId) {
        Long driverId = authService.getAuthenticatedUser().getId();
        return rideRepository.findAllByDriverIdAndVehicleId(driverId, vehicleId);
    }


}

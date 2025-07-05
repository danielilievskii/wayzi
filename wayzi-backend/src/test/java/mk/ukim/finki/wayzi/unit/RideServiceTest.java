package mk.ukim.finki.wayzi.unit;

import mk.ukim.finki.wayzi.model.domain.*;
import mk.ukim.finki.wayzi.model.enumeration.Color;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import mk.ukim.finki.wayzi.model.enumeration.VehicleType;
import mk.ukim.finki.wayzi.model.exception.AccessDeniedException;
import mk.ukim.finki.wayzi.model.exception.ResourceNotFoundException;
import mk.ukim.finki.wayzi.repository.RideRepository;
import mk.ukim.finki.wayzi.repository.RideStopRepository;
import mk.ukim.finki.wayzi.service.domain.*;
import mk.ukim.finki.wayzi.service.domain.impl.RideServiceImpl;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideDto;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideStopDto;
import mk.ukim.finki.wayzi.web.dto.ride.UpdateRideDto;
import mk.ukim.finki.wayzi.web.dto.ride.UpdateRideStopDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
public class RideServiceTest {

    @Mock
    private RideRepository rideRepository;
    @Mock
    private AuthService authService;
    @Mock
    private VehicleService vehicleService;
    @Mock
    private LocationService locationService;
    @Mock
    private RideStopRepository rideStopRepository;
    @Mock
    private RouteCoordinatesService routeCoordinatesService;

    @InjectMocks
    private RideServiceImpl rideService;

    private User user1;
    private User user2;
    private Vehicle vehicle1;
    private Vehicle vehicle2;
    private Location departureLocation;
    private Location arrivalLocation;
    private Location stopLocation1;
    private Location stopLocation2;
    private Ride ride;
    private Ride ride2;
    private RideStop rideStop1;
    private RideStop rideStop2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Users
        user1 = TestDataFactory.createUser(1L, "daniel@gmail.com");
        when(authService.getAuthenticatedUser()).thenReturn(user1);

        user2 = TestDataFactory.createUser(2L, "admin@gmail.com");

        // Vehicles
        vehicle1 = TestDataFactory.createVehicle(1L, user1);
        when(vehicleService.findByIdAndCheckOwnership(1L)).thenReturn(vehicle1);

        vehicle2 = TestDataFactory.createVehicle(2L, user2);
        when(vehicleService.findByIdAndCheckOwnership(2L)).thenThrow(AccessDeniedException.class);

        // Locations
        departureLocation = TestDataFactory.createLocation(1L);
        when(locationService.findById(1L)).thenReturn(departureLocation);

        arrivalLocation = TestDataFactory.createLocation(2L);
        when(locationService.findById(2L)).thenReturn(arrivalLocation);

        stopLocation1 = TestDataFactory.createLocation(3L);
        when(locationService.findById(3L)).thenReturn(stopLocation1);

        stopLocation2 = TestDataFactory.createLocation(4L);
        when(locationService.findById(4L)).thenReturn(stopLocation2);

        // Rides
        LocalDateTime now = LocalDateTime.now();
        ride = new Ride(
                departureLocation, "Address 1", now.plusHours(1),
                arrivalLocation, "Address 2", now.plusHours(4),
                user1, vehicle1, 3, 300, RideStatus.PENDING
        );
        ride.setId(10L);
        when(rideRepository.findById(10L)).thenReturn(Optional.of(ride));

        ride2 = new Ride(
                departureLocation, "Address 1", now.plusHours(1),
                arrivalLocation, "Address 2", now.plusHours(4),
                user2, vehicle2, 3, 300, RideStatus.PENDING
        );
        ride2.setId(11L);
        when(rideRepository.findById(11L)).thenReturn(Optional.of(ride2));

        //Ride Stops
        rideStop1 = new RideStop(ride, stopLocation1, "Address 3", now.plusHours(2), 1);
        rideStop1.setId(1L);
        when(rideStopRepository.findById(1L)).thenReturn(Optional.of(rideStop1));

        rideStop2 = new RideStop(ride, stopLocation1, "Address 3", now.plusHours(3), 2);
        rideStop2.setId(2L);
        when(rideStopRepository.findById(2L)).thenReturn(Optional.of(rideStop2));


        when(rideRepository.save(any(Ride.class))).thenAnswer(i -> i.getArguments()[0]);
        when(routeCoordinatesService.fetchCoordinates(any())).thenReturn(Collections.singletonList(List.of(0.0, 0.0)));

    }

    /**
     * Nested test class for testing the save method.
     */
    @Nested
    class SaveTests {

        /**
         * Tests the successful creation of a ride.
         */
        @Test
        void shouldSaveRide() {
            LocalDateTime now = LocalDateTime.now();

            CreateRideDto dto = new CreateRideDto(
                    departureLocation.getId(), now.plusHours(1), "Address 1",
                    arrivalLocation.getId(), now.plusHours(3), "Address 2",
                    1L, 3, 250,
                    Collections.emptyList()
            );

            Ride result = rideService.save(dto);

            assertNotNull(result);
            assertEquals(departureLocation, result.getDepartureLocation());
            assertEquals(arrivalLocation, result.getArrivalLocation());
            assertEquals(vehicle1, result.getVehicle());
            assertEquals(3, result.getAvailableSeats());
            assertEquals(250.0, result.getPricePerSeat());
            assertEquals(0, result.getRideStops().size());

            verify(rideRepository).save(any(Ride.class));
        }

        /**
         * Tests that ResourceNotFoundException is thrown when the vehicle is not found.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowResourceNotFoundException_whenVehicleNotFound() {
            LocalDateTime now = LocalDateTime.now();
            Long vehicleId = 999L;
            when(vehicleService.findByIdAndCheckOwnership(vehicleId))
                    .thenThrow(new ResourceNotFoundException(String.format("Vehicle with id: %d was not found.", vehicleId)));

            CreateRideDto dto = new CreateRideDto(
                    departureLocation.getId(), now.plusHours(3), "Address 1",
                    arrivalLocation.getId(), now.plusHours(1), "Address 2",
                    vehicleId, 3, 200,
                    Collections.emptyList()
            );

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> rideService.save(dto));
            assertEquals(String.format("Vehicle with id: %d was not found.", vehicleId), exception.getMessage());
        }

        /**
         * Tests that AccessDeniedException is thrown when the user tries to use a vehicle they don't own.
         */
        @Test
        void shouldThrowAccessDeniedException_whenUserDoesNotOwnTheVehicle() {
            LocalDateTime now = LocalDateTime.now();
            Long vehicleId = 2L;

            CreateRideDto dto = new CreateRideDto(
                    departureLocation.getId(), now.plusHours(3), "Address 1",
                    arrivalLocation.getId(), now.plusHours(1), "Address 2",
                    vehicleId, 3, 200,
                    Collections.emptyList()
            );

            assertThrows(AccessDeniedException.class, () -> rideService.save(dto));
        }

        /**
         * Tests that IllegalArgumentException is thrown when arrival time is before departure time.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowIllegalArgumentException_whenArrivalTimeBeforeDepartureTime() {
            LocalDateTime now = LocalDateTime.now();

            CreateRideDto dto = new CreateRideDto(
                    departureLocation.getId(), now.plusHours(3), "Address 1",
                    arrivalLocation.getId(), now.plusHours(1), "Address 2",
                    1L, 3, 200,
                    Collections.emptyList()
            );

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rideService.save(dto));
            assertEquals("Departure time can't be after arrival time.", exception.getMessage());
        }

        /**
         * Tests that IllegalArgumentException is thrown when arrival time is invalid.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowIllegalArgumentException_whenInvalidArrivalTime() {
            LocalDateTime now = LocalDateTime.now();

            CreateRideDto dto = new CreateRideDto(
                    departureLocation.getId(), now.plusHours(3), "Address 1",
                    arrivalLocation.getId(), now.minusHours(3), "Address 2",
                    1L, 3, 200,
                    Collections.emptyList()
            );

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rideService.save(dto));
            assertEquals("Invalid departure or arrival time.", exception.getMessage());
        }

        /**
         * Tests that IllegalArgumentException is thrown when a stop time is outside the time bounds.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowIllegalArgumentException_whenStopTimeOutsideTimeBounds() {
            LocalDateTime now = LocalDateTime.now();

            CreateRideDto dto = new CreateRideDto(
                    departureLocation.getId(), now.plusHours(1), "Address 1",
                    arrivalLocation.getId(), now.plusHours(2), "Address 2",
                    1L, 3, 200,
                    List.of(
                            new CreateRideStopDto(stopLocation1.getId(), "Address 3", now.plusHours(3), 1)
                    )
            );

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rideService.save(dto));
            assertEquals("Stop time no. 1 must be between departure and arrival time.", exception.getMessage());

        }

        /**
         * Tests that IllegalArgumentException is thrown when stop times are out of order.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowIllegalArgumentException_whenStopTimesOutOfOrder() {
            LocalDateTime now = LocalDateTime.now();

            CreateRideDto dto = new CreateRideDto(
                    departureLocation.getId(), now.plusHours(1), "Address 1",
                    arrivalLocation.getId(), now.plusHours(5), "Address 2",
                    1L, 3, 200,
                    List.of(
                            new CreateRideStopDto(stopLocation1.getId(), "Address 3", now.plusHours(3), 1),
                            new CreateRideStopDto(stopLocation2.getId(), "Address 4", now.plusHours(2), 2)
                    )
            );

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rideService.save(dto));
            assertEquals("Stop no. 1 has later time than stop no. 2", exception.getMessage());
        }

    }


    /**
     * Nested test class for testing the edit method.
     */
    @Nested
    class EditTests {

        /**
         * Tests editing a ride when the price is changed and a ride stop is deleted.
         */
        @Test
        void shouldEditRide_whenPriceChangedAndRideStopDeleted() {
            LocalDateTime now = LocalDateTime.now();

            UpdateRideDto dto = new UpdateRideDto(
                    departureLocation.getId(), now.plusHours(1), "Address 1",
                    arrivalLocation.getId(), now.plusHours(3), "Address 2",
                    1L, 3, 500,
                    List.of(
                            new UpdateRideStopDto(rideStop2.getId(), rideStop2.getLocation().getId(), "Address 3", rideStop2.getStopTime(), 1)
                    )
            );
            Ride result = rideService.edit(ride.getId(), dto);

            assertNotNull(result);
            assertEquals(1L, result.getDepartureLocation().getId());
            assertEquals(2L, result.getArrivalLocation().getId());
            assertEquals(1L, result.getVehicle().getId());
            assertEquals(3, result.getAvailableSeats());
            assertEquals(500, result.getPricePerSeat());
            assertEquals(1, result.getRideStops().size());
        }

        /**
         * Tests editing a ride when a ride stop is added.
         */
        @Test
        void shouldEditRide_RideStopAdded() {
            LocalDateTime now = LocalDateTime.now();
            Long id = ride.getId();

            UpdateRideDto dto = new UpdateRideDto(
                    departureLocation.getId(), now.plusHours(1), "Address 1",
                    arrivalLocation.getId(), now.plusHours(3), "Address 2",
                    vehicle1.getId(), 3, 500,
                    List.of(
                            new UpdateRideStopDto(rideStop1.getId(), rideStop1.getLocation().getId(), "Address 3", rideStop1.getStopTime(), 1),
                            new UpdateRideStopDto(rideStop2.getId(), rideStop2.getLocation().getId(), "Address 4", rideStop2.getStopTime(), 2),
                            new UpdateRideStopDto(null, stopLocation2.getId(), "Address 5", rideStop2.getStopTime().plusMinutes(30), 3)
                    )
            );
            Ride result = rideService.edit(id, dto);
            assertEquals(3, result.getRideStops().size());
        }

        /**
         * Tests that AccessDeniesException is thrown when the user tries to edit a ride they don't own.
         */
        @Test
        void shouldThrowAccessDeniesException_whenUserDoesNotOwnTheRide() {
            LocalDateTime now = LocalDateTime.now();
            Long id = ride2.getId();

            UpdateRideDto dto = new UpdateRideDto(
                    departureLocation.getId(), now.plusHours(1), "Address 1",
                    arrivalLocation.getId(), now.plusHours(3), "Address 2",
                    vehicle1.getId(), 3, 500,
                    List.of()
            );

            assertThrows(AccessDeniedException.class, () -> rideService.edit(id, dto));
            verify(rideRepository).findById(id);
        }

        /**
         * Tests that ResourceNotFoundException is thrown when the ride is not found.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowResourceNotFoundException_whenRideNotFound() {
            LocalDateTime now = LocalDateTime.now();
            Long id = 999L;

            UpdateRideDto dto = new UpdateRideDto(
                    departureLocation.getId(), now.plusHours(1), "Address 1",
                    arrivalLocation.getId(), now.plusHours(3), "Address 2",
                    vehicle1.getId(), 3, 500,
                    List.of()
            );
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> rideService.edit(id, dto));
            assertEquals(String.format("Ride with id: %d was not found.", id), exception.getMessage());
            verify(rideRepository).findById(id);

        }

        /**
         * Tests that ResourceNotFoundException is thrown when the vehicle is not found.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowResourceNotFoundException_whenVehicleNotFound() {
            LocalDateTime now = LocalDateTime.now();
            Long id = ride.getId();

            Long vehicleId = 999L;
            when(vehicleService.findByIdAndCheckOwnership(vehicleId))
                    .thenThrow(new ResourceNotFoundException(String.format("Vehicle with id: %d was not found.", vehicleId)));

            UpdateRideDto dto = new UpdateRideDto(
                    departureLocation.getId(), now.plusHours(1), "Address 1",
                    arrivalLocation.getId(), now.plusHours(3), "Address 2",
                    vehicleId, 3, 500,
                    List.of()
            );


            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> rideService.edit(id, dto));
            assertEquals(String.format("Vehicle with id: %d was not found.", vehicleId), exception.getMessage());
            verify(vehicleService).findByIdAndCheckOwnership(vehicleId);
        }

        /**
         * Tests that ResourceNotFoundException is thrown when the location is not found.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowResourceNotFoundException_whenLocationNotFound() {
            LocalDateTime now = LocalDateTime.now();
            Long id = ride.getId();

            Long locationId = 999L;
            when(locationService.findById(locationId))
                    .thenThrow(new ResourceNotFoundException(String.format("Location with id: %d was not found.", locationId)));

            UpdateRideDto dto = new UpdateRideDto(
                    locationId, now.plusHours(1), "Address 1",
                    arrivalLocation.getId(), now.plusHours(3), "Address 2",
                    vehicle1.getId(), 3, 500,
                    List.of()
            );
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> rideService.edit(id, dto));
            assertEquals(String.format("Location with id: %d was not found.", locationId), exception.getMessage());
            verify(locationService).findById(locationId);

        }

        /**
         * Tests that ResourceNotFoundException is thrown when the ride stop is not found.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowResourceNotFoundException_whenRideStopNotFound() {
            LocalDateTime now = LocalDateTime.now();
            Long id = ride.getId();

            Long rideStopId = 999L;
            when(rideStopRepository.findById(rideStopId))
                    .thenThrow(new ResourceNotFoundException(String.format("Ride stop with id: %d was not found", rideStopId)));

            UpdateRideDto dto = new UpdateRideDto(
                    departureLocation.getId(), now.plusHours(1), "Address 1",
                    arrivalLocation.getId(), now.plusHours(3), "Address 2",
                    vehicle1.getId(), 3, 500,
                    List.of(
                            new UpdateRideStopDto(rideStopId, rideStop1.getLocation().getId(), "Address 3", rideStop1.getStopTime(), 1)
                    )
            );
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> rideService.edit(id, dto));
            assertEquals(String.format("Ride stop with id: %d was not found", 999L), exception.getMessage());
            verify(rideStopRepository).findById(rideStopId);
        }
    }

    /**
     * Nested test class for testing the findPage method.
     */
    @Nested
    class FindPageTests {

    }

    /**
     * Nested test class for testing the findById method.
     */
    @Nested
    class FindByIdTests {

        /**
         * Tests that a ride is returned when found by ID.
         */
        @Test
        void shouldReturnRide() {
            Ride ride = new Ride();
            ride.setId(1L);

            when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));
            assertEquals(ride, rideService.findById(1L));
        }

        /**
         * Tests that ResourceNotFoundException is thrown when the ride is not found.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowResourceNotFoundException_whenRideNotFound() {
            when(rideRepository.findById(5L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> rideService.findById(5L));
            assertEquals(String.format("Ride with id: %d was not found.", 5L), exception.getMessage());
        }
    }

    /**
     * Nested test class for testing the findByIdAndCheckOwnership method.
     */
    @Nested
    class FindByIdAndCheckOwnershipTests {

        /**
         * Tests that a ride is returned when the user is authorized.
         */
        @Test
        void shouldReturnRide_whenAuthorized() {
            Ride ride = new Ride();
            ride.setDriver(user1);
            ride.setId(1L);

            when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));
            assertEquals(ride, rideService.findByIdAndCheckOwnership(1L));
        }

        /**
         * Tests that AccessDeniedException is thrown when the user is unauthorized.
         */
        @Test
        void shouldThrowAccessDeniedException_whenUnauthorized() {
            User user2 = new User();
            user2.setId(2L);

            Ride ride = new Ride();
            ride.setDriver(user2);
            ride.setId(1L);

            when(rideRepository.findById(1L)).thenReturn(Optional.of(ride));
            assertThrows(AccessDeniedException.class, () -> rideService.findByIdAndCheckOwnership(1L));
        }
    }

    /**
     * Nested test class for testing the findAllByVehicleIdForUser method.
     */
    @Nested
    class FindAllByVehicleIdForUserTests {

        /**
         * Tests that rides are returned for a user by vehicle ID.
         */
        @Test
        void shouldReturnRidesForUserByVehicleId() {
            Long vehicleId = 1L;
            Long driverId = user1.getId();

            Ride ride1 = new Ride();
            ride1.setId(10L);
            ride1.setDriver(user1);
            ride1.setVehicle(vehicle1);

            Ride ride2 = new Ride();
            ride2.setId(11L);
            ride2.setDriver(user1);
            ride2.setVehicle(vehicle1);

            List<Ride> expectedRides = List.of(ride1, ride2);
            when(rideRepository.findAllByDriverIdAndVehicleId(driverId, vehicleId)).thenReturn(expectedRides);

            List<Ride> result = rideService.findAllByVehicleIdForUser(vehicleId);

            assertEquals(2, result.size());
            assertEquals(expectedRides, result);
            verify(rideRepository).findAllByDriverIdAndVehicleId(driverId, vehicleId);
        }

        /**
         * Tests that an empty list is returned when no rides are found for the user and vehicle.
         */
        @Test
        void shouldReturnEmptyList_whenNoRidesFound() {
            Long vehicleId = vehicle1.getId();
            Long driverId = user1.getId();

            when(rideRepository.findAllByDriverIdAndVehicleId(driverId, vehicleId)).thenReturn(Collections.emptyList());

            List<Ride> result = rideService.findAllByVehicleIdForUser(vehicleId);

            assertTrue(result.isEmpty());
            verify(rideRepository).findAllByDriverIdAndVehicleId(driverId, vehicleId);
        }
    }
}




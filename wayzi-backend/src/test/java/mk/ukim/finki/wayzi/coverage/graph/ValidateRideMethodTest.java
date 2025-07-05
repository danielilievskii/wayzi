package mk.ukim.finki.wayzi.coverage.graph;

import mk.ukim.finki.wayzi.model.domain.Location;
import mk.ukim.finki.wayzi.model.domain.User;
import mk.ukim.finki.wayzi.model.domain.Vehicle;
import mk.ukim.finki.wayzi.service.domain.impl.RideServiceImpl;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideDto;
import mk.ukim.finki.wayzi.web.dto.ride.CreateRideStopDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ValidateRideMethodTest {

    @InjectMocks
    private RideServiceImpl rideService;

    private User user;
    private Vehicle vehicle;
    private Location departureLocation;
    private Location arrivalLocation;
    private Location stopLocation1;
    private Location stopLocation2;
    private Location stopLocation3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setCapacity(4);

        departureLocation = new Location();
        departureLocation.setId(1L);

        arrivalLocation = new Location();
        arrivalLocation.setId(2L);

        stopLocation1 = new Location();
        stopLocation1.setId(3L);
        stopLocation2 = new Location();
        stopLocation2.setId(4L);
        stopLocation3 = new Location();
        stopLocation3.setId(5L);
    }

    @Nested
    class ValidateRideMethod {

        @Test
        void ID_2_withInvalidStopTime_shouldThrowException() {
            LocalDateTime now = LocalDateTime.now();

            CreateRideDto dto = new CreateRideDto(
                    1L, now.plusHours(1), "Address 1",
                    2L, now.plusHours(5), "Address 2",
                    1L, 3, 250,
                    List.of(
                            new CreateRideStopDto(3L, "Address 3", now.plusHours(2), 1),
                            new CreateRideStopDto(4L, "Address 4", now.plusHours(4), 2),
                            new CreateRideStopDto(5L, "Address 2", now.plusHours(3), 3)
                    )
            );

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rideService.validateRide(dto));
            assertEquals("Stop no. 2 has later time than stop no. 3", exception.getMessage());
        }

        @Test
        void ID_3_withInvalidDepartureTime_shouldThrowException() {
            LocalDateTime now = LocalDateTime.now();

            CreateRideDto dto = new CreateRideDto(
                    1L, now.minusHours(2), "Address 1",
                    2L, now.plusHours(5), "Address 2",
                    1L, 3, 250,
                    List.of()
            );

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rideService.validateRide(dto));
            assertEquals("Invalid departure or arrival time.", exception.getMessage());
        }

        @Test
        void ID_4_withArrivalTimeBeforeDepartureTime_shouldThrowException() {
            LocalDateTime now = LocalDateTime.now();

            CreateRideDto dto = new CreateRideDto(
                    1L, now.plusHours(5), "Address 1",
                    2L, now.plusHours(2), "Address 2",
                    1L, 3, 200,
                    List.of()
            );

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rideService.validateRide(dto));
            assertEquals("Departure time can't be after arrival time.", exception.getMessage());
        }

        @Test
        void ID_7_withValidInput() {
            LocalDateTime now = LocalDateTime.now();

            CreateRideDto dto = new CreateRideDto(
                    1L, now.plusHours(2), "Address 1",
                    2L, now.plusHours(5), "Address 2",
                    1L, 3, 200,
                    List.of()
            );

            assertDoesNotThrow(() -> rideService.validateRide(dto));
        }

        @Test
        void ID_11_withStopTimeOutsideTimeBounds_shouldThrowException() {
            LocalDateTime now = LocalDateTime.now();

            CreateRideDto dto = new CreateRideDto(
                    1L, now.plusHours(3), "Address 1",
                    2L, now.plusHours(5), "Address 2",
                    1L, 3, 200,
                    List.of(
                            new CreateRideStopDto(3L, "Address 3", now.plusHours(2), 1)
                    )
            );

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rideService.validateRide(dto));
            assertEquals("Stop time no. 1 must be between departure and arrival time.", exception.getMessage());
        }

        @Test
        void ID_12_withStopTimeOutsideTimeBounds_shouldThrowException() {
            LocalDateTime now = LocalDateTime.now();

            CreateRideDto dto = new CreateRideDto(
                    1L, now.plusHours(3), "Address 1",
                    2L, now.plusHours(5), "Address 2",
                    1L, 3, 200,
                    List.of(
                            new CreateRideStopDto(3L, "Address 1", now.plusHours(4), 1),
                            new CreateRideStopDto(3L, "Address 2", now.plusHours(7), 2)
                    )
            );

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rideService.validateRide(dto));
            assertEquals("Stop time no. 2 must be between departure and arrival time.", exception.getMessage());
        }

        @Test
        void ID_14_withOutOfOrderStops_shouldThrowException() {
            LocalDateTime now = LocalDateTime.now();

            CreateRideDto dto = new CreateRideDto(
                    1L, now.plusHours(1), "Address 1",
                    2L, now.plusHours(5), "Address 2",
                    1L, 3, 200,
                    List.of(
                            new CreateRideStopDto(3L, "Address 1", now.plusHours(3), 1),
                            new CreateRideStopDto(3L, "Address 2", now.plusHours(2), 2)
                    )
            );

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> rideService.validateRide(dto));
            assertEquals("Stop no. 1 has later time than stop no. 2", exception.getMessage());
        }

    }

}

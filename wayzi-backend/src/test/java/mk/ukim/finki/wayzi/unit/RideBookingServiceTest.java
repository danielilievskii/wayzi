package mk.ukim.finki.wayzi.unit;

import mk.ukim.finki.wayzi.model.domain.*;
import mk.ukim.finki.wayzi.model.dto.MailSendingStatus;
import mk.ukim.finki.wayzi.model.enumeration.*;
import mk.ukim.finki.wayzi.model.exception.AccessDeniedException;
import mk.ukim.finki.wayzi.model.exception.ResourceNotFoundException;
import mk.ukim.finki.wayzi.repository.RideBookingRepository;
import mk.ukim.finki.wayzi.repository.RideRepository;
import mk.ukim.finki.wayzi.service.domain.*;
import mk.ukim.finki.wayzi.service.domain.impl.RideBookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
public class RideBookingServiceTest {

    @Mock
    private RideService rideService;
    @Mock
    private AuthService authService;
    @Mock
    private RideBookingRepository rideBookingRepository;
    @Mock
    private RideRepository rideRepository;
    @Mock
    private QRCodeService qrCodeService;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private RideBookingServiceImpl rideBookingService;

    private User user1;
    private User user2;
    private Vehicle vehicle1;
    private Vehicle vehicle2;
    private Location departureLocation;
    private Location arrivalLocation;
    private Location stopLocation1;
    private Location stopLocation2;
    private Ride ride1;
    private Ride ride2;
    private RideStop rideStop1;
    private RideStop rideStop2;
    private RideBooking rideBooking1;
    private RideBooking rideBooking2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Users
        user1 = TestDataFactory.createUser(1L, "daniel@gmail.com");
        when(authService.getAuthenticatedUser()).thenReturn(user1);

        user2 = TestDataFactory.createUser(2L, "admin@gmail.com");

        // Vehicles
        vehicle1 = TestDataFactory.createVehicle(1L, user1);
        vehicle2 = TestDataFactory.createVehicle(2L, user2);

        // Locations
        departureLocation = TestDataFactory.createLocation(1L);
        arrivalLocation = TestDataFactory.createLocation(2L);
        stopLocation1 = TestDataFactory.createLocation(3L);
        stopLocation2 = TestDataFactory.createLocation(4L);

        // Rides
        LocalDateTime now = LocalDateTime.now();
        ride1 = new Ride(
                departureLocation, "Address 1", now.plusHours(1),
                arrivalLocation, "Address 2", now.plusHours(4),
                user1, vehicle1, 3, 300, RideStatus.PENDING
        );
        ride1.setId(10L);
        when(rideService.findById(10L)).thenReturn(ride1);

        ride2 = new Ride(
                departureLocation, "Address 1", now.plusHours(1),
                arrivalLocation, "Address 2", now.plusHours(4),
                user2, vehicle2, 3, 300, RideStatus.PENDING
        );
        ride2.setId(11L);
        when(rideService.findById(11L)).thenReturn(ride2);

        //Ride Stops
        rideStop1 = new RideStop(ride1, stopLocation1, "Address 3", now.plusHours(2), 1);
        rideStop1.setId(1L);

        rideStop2 = new RideStop(ride1, stopLocation1, "Address 3", now.plusHours(3), 2);
        rideStop2.setId(2L);

        //Ride Bookings
        rideBooking1 = TestDataFactory.createRideBooking(1L, ride1, user2, 1, now);
        when(rideBookingRepository.findById(1L)).thenReturn(Optional.of(rideBooking1));

        rideBooking2 = TestDataFactory.createRideBooking(2L, ride2, user1, 1, now);
        when(rideBookingRepository.findById(2L)).thenReturn(Optional.of(rideBooking2));

        when(rideRepository.save(any(Ride.class))).thenAnswer(i -> i.getArguments()[0]);
        when(rideBookingRepository.save(any(RideBooking.class))).thenAnswer(i -> i.getArguments()[0]);

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
        void shouldReturnRideBooking() {
            assertEquals(rideBooking1, rideBookingService.findById(1L));
            verify(rideBookingRepository).findById(rideBooking1.getId());
        }

        /**
         * Tests that ResourceNotFoundException is thrown when the ride booking is not found.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowResourceNotFoundException_whenRideBookingNotFound() {
            Long rideBookingId = 999L;
            when(rideRepository.findById(rideBookingId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> rideBookingService.findById(rideBookingId));
            assertEquals(String.format("Ride booking with id: %d was not found.", rideBookingId), exception.getMessage());

            verify(rideBookingRepository).findById(999L);
        }
    }

    /**
     * Nested test class for testing the findByIdEnsuringBookerOwnership method.
     */
    @Nested
    class FindByIdEnsuringBookerOwnershipTests {

        /**
         * Tests that a ride booking is returned when the booker is authorized.
         */
        @Test
        void shouldReturnRideBooking_whenBookerIsAuthorized() {
            assertEquals(rideBooking2, rideBookingService.findByIdEnsuringBookerOwnership(rideBooking2.getId()));

            verify(rideBookingRepository).findById(rideBooking2.getId());
            verify(authService).getAuthenticatedUser();
        }

        /**
         * Tests that AccessDeniedException is thrown when the booker is unauthorized.
         */
        @Test
        void shouldThrowAccessDeniedException_whenBookerIsUnauthorized() {
            assertThrows(AccessDeniedException.class, () -> rideBookingService.findByIdEnsuringBookerOwnership(rideBooking1.getId()));

            verify(rideBookingRepository).findById(rideBooking1.getId());
            verify(authService).getAuthenticatedUser();
        }
    }

    /**
     * Nested test class for testing the findByIdEnsuringBookerOwnership method.
     */
    @Nested
    class FindByIdEnsuringDriverOwnershipTests {

        /**
         * Tests that a ride booking is returned when the driver is authorized.
         */
        @Test
        void shouldReturnRideBooking_whenDriverIsAuthorized() {
            assertEquals(rideBooking1, rideBookingService.findByIdEnsuringDriverOwnership(rideBooking1.getId()));

            verify(rideBookingRepository).findById(rideBooking1.getId());
            verify(authService).getAuthenticatedUser();
        }

        /**
         * Tests that AccessDeniedException is thrown when the driver is unauthorized.
         */
        @Test
        void shouldThrowAccessDeniedException_whenBookerIsUnauthorized() {
            assertThrows(AccessDeniedException.class, () -> rideBookingService.findByIdEnsuringDriverOwnership(rideBooking2.getId()));

            verify(rideBookingRepository).findById(rideBooking2.getId());
            verify(authService).getAuthenticatedUser();
        }
    }

    /**
     * Nested test class for testing the findPageForUser method.
     */
    @Nested
    class FindPageForUserTests {

    }


    /**
     * Nested test class for testing the bookRide method.
     */
    @Nested
    class BookRideTests {

        @BeforeEach
        void setUp() {
            when(qrCodeService.generateQRCodeBase64("qrcode", 200, 200)).thenReturn("data:qrcode");

            MailSendingStatus status = new MailSendingStatus(
                    true, new String[] {"driver@example.com"}, "New Booking Confirmed", null
            );
            CompletableFuture<MailSendingStatus> future = CompletableFuture.completedFuture(status);

            when(notificationService.notifyDriverOfNewBooking(any(RideBooking.class)))
                    .thenReturn(List.of(future));
        }

        /**
         * Tests the successful booking of a ride.
         */
        @Test
        void shouldSaveRide() {
            int availableSeats = ride2.getAvailableSeats();
            int bookingSeats = 1;

            RideBooking result = rideBookingService.bookRide(
                    ride2.getId(), PaymentMethod.CASH, bookingSeats, "Message."
            );

            assertNotNull(result);
            assertEquals(ride2, result.getRide());
            assertEquals(user1, result.getBooker());
            assertEquals(PaymentMethod.CASH, result.getPaymentMethod());
            assertEquals(RideBookingStatus.CONFIRMED, result.getBookingStatus());
            assertEquals(CheckInStatus.NOT_CHECKED_IN, result.getCheckInStatus());
            assertEquals(1, result.getBookedSeats());
            assertEquals(ride2.getPricePerSeat()*result.getBookedSeats(), result.getTotalPrice());

            assertEquals(ride2.getAvailableSeats(), availableSeats - bookingSeats);

            verify(rideService).findById(ride2.getId());
            verify(authService).getAuthenticatedUser();
            verify(rideRepository).save(ride2);
            verify(rideBookingRepository, times(2)).save(any(RideBooking.class));
            verify(notificationService).notifyDriverOfNewBooking(result);
        }

        /**
         * Tests that IllegalStateException is thrown when a user attempts to book their own ride.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowIllegalStateException_whenBookingOwnRide() {

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> rideBookingService.bookRide(ride1.getId(), PaymentMethod.CASH, 1, "msg")
            );
            assertEquals("You cannot book your own ride.", exception.getMessage());
        }

        /**
         * Tests that IllegalStateException is thrown when a user attempts to book a ride
         * they have already booked.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowIllegalStateException_whenUserAlreadyBookedRide() {
            ride2.setRideBookings(List.of(rideBooking2));

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> rideBookingService.bookRide(ride2.getId(), PaymentMethod.CASH, 1, "msg")
            );

            assertEquals("You already have an active booking for this ride.", exception.getMessage());
        }

        /**
         * Tests that IllegalStateException is thrown when attempting to book a ride
         * whose status is STARTED, FINISHED or CANCELLED.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowIllegalStateException_whenRideStatusIsInvalid() {
            ride2.setStatus(RideStatus.CANCELLED);

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> rideBookingService.bookRide(ride2.getId(), PaymentMethod.CASH, 1, "msg")
            );

            assertEquals("You cannot book this ride anymore.", exception.getMessage());
        }

        /**
         * Tests that IllegalStateException is thrown when attempting to book a ride
         *  whose departure time is in the past.
         *  Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowIllegalStateException_whenDepartureTimeIsInPast() {
            ride2.setDepartureTime(LocalDateTime.now().minusMinutes(5));

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> rideBookingService.bookRide(ride2.getId(), PaymentMethod.CASH, 1, "msg")
            );

            assertEquals("You cannot book this ride anymore.", exception.getMessage());
        }

        /**
         * Tests that IllegalStateException is thrown when attempting to book
         * more seats than are currently available for the ride.
         * Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowIllegalArgumentException_whenBookingTooManySeats() {
            ride2.setAvailableSeats(1);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> rideBookingService.bookRide(ride2.getId(), PaymentMethod.CASH, 5, "msg")
            );

            assertEquals("You cannot book more seats than are available.", exception.getMessage());
        }
    }

    /**
     * Nested test class for testing the cancelRideBooking method.
     */
    @Nested
    class CancelRideBookingTests {

        /**
         * Tests the successful cancellation of a ride booking.
         */
        @Test
        void shouldCancelRideBooking() {
            int availableSeats = ride2.getAvailableSeats();

            RideBooking result = rideBookingService.cancelRideBooking(rideBooking2.getId());

            assertNotNull(result);
            assertEquals(RideBookingStatus.CANCELLED, result.getBookingStatus());
            assertEquals(CheckInStatus.NOT_CHECKED_IN, result.getCheckInStatus());
            assertEquals(availableSeats+rideBooking2.getBookedSeats(), result.getRide().getAvailableSeats());

            verify(rideService).save(rideBooking2.getRide());
            verify(rideBookingRepository).save(any(RideBooking.class));
            verify(notificationService).notifyDriverOfBookingCancellation(result);
        }

        /**
         * Tests that IllegalStateException is thrown when attempting to cancel a booking
         *  that has already been cancelled.
         *  Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowIllegalStateException_whenCancellingAlreadyCancelledBooking() {
            rideBooking2.setBookingStatus(RideBookingStatus.CANCELLED);

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> rideBookingService.cancelRideBooking(rideBooking2.getId())
            );

            assertEquals("You cannot cancel a ride twice.", exception.getMessage());
        }

        /**
         * Tests that IllegalStateException is thrown when attempting to cancel a booking
         *  that whose ride has already started.
         *  Verifies that the exception message contains the expected error.
         */
        @Test
        void shouldThrowIllegalStateException_whenCancellingRideAlreadyStarted() {
            ride2.setStatus(RideStatus.STARTED);

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> rideBookingService.cancelRideBooking(rideBooking2.getId())
            );

            assertEquals("Ride cancellation not possible.", exception.getMessage());
        }
    }





}




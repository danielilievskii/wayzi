package mk.ukim.finki.wayzi.service.domain.impl;

import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.model.domain.RideBooking;
import mk.ukim.finki.wayzi.model.domain.User;
import mk.ukim.finki.wayzi.model.enumeration.CheckInStatus;
import mk.ukim.finki.wayzi.model.enumeration.PaymentMethod;
import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import mk.ukim.finki.wayzi.model.exception.*;
import mk.ukim.finki.wayzi.model.exception.ResourceNotFoundException;
import mk.ukim.finki.wayzi.repository.RideBookingRepository;
import mk.ukim.finki.wayzi.repository.RideRepository;
import mk.ukim.finki.wayzi.service.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static mk.ukim.finki.wayzi.specifications.FieldFilterSpecification.*;

@Service
public class RideBookingServiceImpl implements RideBookingService {

    private final RideService rideService;
    private final AuthService authService;
    private final RideBookingRepository rideBookingRepository;
    private final RideRepository rideRepository;
    private final QRCodeService qrCodeService;
    private final NotificationService notificationService;

    public RideBookingServiceImpl(RideService rideService,
                                  AuthService authService,
                                  RideBookingRepository rideBookingRepository,
                                  RideRepository rideRepository,
                                  QRCodeService qrCodeService,
                                  NotificationService notificationService
    ) {
        this.rideService = rideService;
        this.authService = authService;
        this.rideBookingRepository = rideBookingRepository;
        this.rideRepository = rideRepository;
        this.qrCodeService = qrCodeService;
        this.notificationService = notificationService;
    }

    @Override
    public RideBooking findById(Long rideBookingId) {
        return rideBookingRepository.findById(rideBookingId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Ride booking with id: %d was not found.", rideBookingId)));
    }

    @Override
    public RideBooking findByIdEnsuringBookerOwnership(Long rideBookingId) {
        RideBooking rideBooking = findById(rideBookingId);
        User currentUser = authService.getAuthenticatedUser();

        if(!rideBooking.getBooker().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException();
        }

        return rideBooking;
    }

    @Override
    public RideBooking findByIdEnsuringDriverOwnership(Long rideBookingId) {
        RideBooking rideBooking = findById(rideBookingId);
        User currentUser = authService.getAuthenticatedUser();

        if(!rideBooking.getRide().getDriver().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException();
        }

        return rideBooking;
    }


    @Override
    public Page<RideBooking> findPageForUser(RideBookingStatus status, Integer pageNum, Integer pageSize) {
        Long userId = authService.getAuthenticatedUser().getId();

        Specification<RideBooking> specification = Specification
                .where(filterEquals(RideBooking.class, "booker.id", userId));

        if(status != null) {
            specification.and(filterEqualsV(RideBooking.class, "bookingStatus", status));
        }

        Page<RideBooking> page  = this.rideBookingRepository.findAll(
                specification,
                PageRequest.of(pageNum - 1, pageSize));


        return page;
    }

    @Override
    public RideBooking save(RideBooking rideBooking) {
        return rideBookingRepository.save(rideBooking);
    }

    @Override
    @Transactional
    public RideBooking bookRide(Long rideId, PaymentMethod paymentMethod, Integer bookedSeats, String message) {
        Ride ride = rideService.findById(rideId);
        User user = authService.getAuthenticatedUser();

        validateBookingRequest(ride, user, bookedSeats);
        ride.setAvailableSeats(ride.getAvailableSeats() - bookedSeats);
        rideRepository.save(ride);

        Integer totalPrice = calculateTotalPrice(ride.getPricePerSeat(), bookedSeats);
        LocalDateTime bookingTime = LocalDateTime.now();

        RideBooking rideBooking = new RideBooking();
        rideBooking.setRide(ride);
        rideBooking.setBooker(user);
        rideBooking.setPaymentMethod(paymentMethod);
        rideBooking.setBookingStatus(RideBookingStatus.CONFIRMED);
        rideBooking.setCheckInStatus(CheckInStatus.NOT_CHECKED_IN);
        rideBooking.setBookedSeats(bookedSeats);
        rideBooking.setTotalPrice(totalPrice);
        rideBooking.setMessage(message);
        rideBooking.setBookingTime(bookingTime);

        RideBooking savedBooking = rideBookingRepository.save(rideBooking);
        String bookingUrl = "http://localhost:5173/rides/bookings/" + savedBooking.getId() + "/check-in";
        String qrCodeUrl = qrCodeService.generateQRCodeBase64(bookingUrl, 200, 200);
        savedBooking.setQrCodeUrl(qrCodeUrl);

        notificationService.notifyDriverOfNewBooking(savedBooking);

        return rideBookingRepository.save(rideBooking);
    }


    private void validateBookingRequest(Ride ride, User user, Integer bookedSeats) {
        if (isUserBookingOwnRide(ride.getDriver(), user)) {
            throw new IllegalStateException("You cannot book your own ride.");
        }

        if(hasUserAlreadyBooked(ride, user)) {
            throw new IllegalStateException("You already have an active booking for this ride.");
        }

        if (!ride.getStatus().name().equals("PENDING") && !ride.getStatus().name().equals("CONFIRMED")) {
            throw new IllegalStateException("You cannot book this ride anymore.");
        }

        if(LocalDateTime.now().isAfter(ride.getDepartureTime())) {
            throw new IllegalStateException("You cannot book this ride anymore.");
        }

        if (bookedSeats > ride.getAvailableSeats()) {
            throw new IllegalArgumentException("You cannot book more seats than are available.");
        }
    }

    private boolean hasUserAlreadyBooked(Ride ride, User user) {
        for(RideBooking rideBooking : ride.getActiveRideBookings()) {
            if(rideBooking.getBooker().getId() == user.getId()) {
                return true;
            }
        }
        return false;
    }

    private boolean isUserBookingOwnRide(User driver, User booker) {
        return driver.getId().equals(booker.getId());
    }

    private Integer calculateTotalPrice(Integer pricePerSeat, Integer bookedSeats) {
        return pricePerSeat * bookedSeats;
    }

    @Override
    @Transactional
    public RideBooking cancelRideBooking(Long rideBookingId) {
        RideBooking rideBooking = findByIdEnsuringBookerOwnership(rideBookingId);
        validateBookingCancellation(rideBooking);

        Ride ride = rideBooking.getRide();
        updateAvailableSeats(ride, rideBooking.getBookedSeats());

        rideBooking.setBookingStatus(RideBookingStatus.CANCELLED);

        notificationService.notifyDriverOfBookingCancellation(rideBooking);

        return rideBookingRepository.save(rideBooking);
    }

    private void validateBookingCancellation(RideBooking rideBooking) {
        if (rideBooking.getBookingStatus() == RideBookingStatus.CANCELLED) {
            throw new IllegalStateException("You cannot cancel a ride twice.");
        }

        RideStatus rideStatus = rideBooking.getRide().getStatus();
        if(rideStatus == RideStatus.STARTED || rideStatus == RideStatus.FINISHED || rideStatus == RideStatus.CANCELLED) {
            throw new IllegalStateException("Ride cancellation not possible.");
        }
    }

    private void updateAvailableSeats(Ride ride, int bookedSeats) {
        ride.setAvailableSeats(ride.getAvailableSeats() + bookedSeats);
        rideService.save(ride);
    }


    @Override
    public RideBooking checkInPassenger(Long rideBookingId) {
        RideBooking rideBooking = findByIdEnsuringDriverOwnership(rideBookingId);
        validatePassengerCheckIn(rideBooking);

        rideBooking.setCheckInStatus(CheckInStatus.CHECKED_IN);
        return rideBookingRepository.save(rideBooking);
    }

    private void validatePassengerCheckIn(RideBooking rideBooking) {
        if (rideBooking.getBookingStatus() == RideBookingStatus.CANCELLED) {
            throw new IllegalStateException("The ride booking is cancelled.");
        }

        RideStatus rideStatus = rideBooking.getRide().getStatus();
        if(rideStatus == RideStatus.STARTED || rideStatus == RideStatus.FINISHED || rideStatus == RideStatus.CANCELLED) {
            throw new IllegalStateException("Passenger check-in not possible.");
        }
    }
}

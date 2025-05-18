package mk.ukim.finki.wayzi.service.domain.impl;

import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.ride.RideBooking;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.model.enumeration.CheckInStatus;
import mk.ukim.finki.wayzi.model.enumeration.PaymentMethod;
import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import mk.ukim.finki.wayzi.model.exception.*;
import mk.ukim.finki.wayzi.repository.RideBookingRepository;
import mk.ukim.finki.wayzi.repository.RideRepository;
import mk.ukim.finki.wayzi.service.domain.AuthService;
import mk.ukim.finki.wayzi.service.domain.QRCodeService;
import mk.ukim.finki.wayzi.service.domain.RideBookingService;
import mk.ukim.finki.wayzi.service.domain.RideService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.time.LocalDateTime;
import java.util.Objects;

import static mk.ukim.finki.wayzi.specifications.FieldFilterSpecification.*;

@Service
public class RideBookingServiceImpl implements RideBookingService {

    private final RideService rideService;
    private final AuthService authService;
    private final RideBookingRepository rideBookingRepository;
    private final RideRepository rideRepository;
    private final QRCodeService qrCodeService;

    public RideBookingServiceImpl(RideService rideService, AuthService authService, RideBookingRepository rideBookingRepository, RideRepository rideRepository, QRCodeService qrCodeService) {
        this.rideService = rideService;
        this.authService = authService;
        this.rideBookingRepository = rideBookingRepository;
        this.rideRepository = rideRepository;
        this.qrCodeService = qrCodeService;
    }

    @Override
    public RideBooking findById(Long rideBookingId) {
        return rideBookingRepository.findById(rideBookingId)
                .orElseThrow(() -> new RideBookingNotFoundException(rideBookingId));
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

        //TODO: Notify driver of new ride booking and send QR code to passenger

        return rideBookingRepository.save(rideBooking);
    }


    private void validateBookingRequest(Ride ride, User user, Integer bookedSeats) {
        if (isDriverBookingOwnRide(ride.getDriver(), user)) {
            throw new RideBookingNotAllowedException("You cannot book your own ride.");
        }

        if (!ride.getStatus().name().equals("PENDING") && !ride.getStatus().name().equals("CONFIRMED")) {
            throw new RideBookingNotAllowedException("You cannot book this ride.");
        }

        if(hasPassengerAlreadyBooked(ride, user)) {
            throw new RideBookingNotAllowedException("You already have an active booking for this ride.");
        }

        if (bookedSeats > ride.getAvailableSeats()) {
            throw new RideBookingNotAllowedException("You cannot book more seats than are available.");
        }
    }

    private boolean hasPassengerAlreadyBooked(Ride ride, User user) {
        for(RideBooking rideBooking : ride.getActiveRideBookings()) {
            if(rideBooking.getBooker().getId() == user.getId()) {
                return true;
            }
        }
        return false;
    }

    private boolean isDriverBookingOwnRide(User driver, User booker) {
        return driver.getId() == booker.getId();
    }

    private Integer calculateTotalPrice(Integer pricePerSeat, Integer bookedSeats) {
        return pricePerSeat * bookedSeats;
    }

    @Override
    public RideBooking cancelRideBooking(Long rideBookingId) {
        RideBooking rideBooking = findByIdEnsuringBookerOwnership(rideBookingId);
        validateBookingCancellation(rideBooking);

        Ride ride = rideBooking.getRide();
        updateAvailableSeats(ride, rideBooking.getBookedSeats());

        rideBooking.setBookingStatus(RideBookingStatus.CANCELLED);
        return rideBookingRepository.save(rideBooking);

        //TODO: Notify driver or ride booking cancellation
    }

    @Override
    public RideBooking checkInPassenger(Long rideBookingId) {
        RideBooking rideBooking = findByIdEnsuringDriverOwnership(rideBookingId);
        validatePassengerCheckIn(rideBooking);

        rideBooking.setCheckInStatus(CheckInStatus.CHECKED_IN);
        return rideBookingRepository.save(rideBooking);
    }

    private void updateAvailableSeats(Ride ride, int bookedSeats) {
        ride.setAvailableSeats(ride.getAvailableSeats() + bookedSeats);
        rideService.save(ride);
    }

    private void validateBookingCancellation(RideBooking rideBooking) {
        if (rideBooking.getBookingStatus() == RideBookingStatus.CANCELLED) {
            throw new RideBookingCancellationNotAllowedException("You cannot cancel a ride twice.");
        }

        RideStatus rideStatus = rideBooking.getRide().getStatus();
        if(rideStatus == RideStatus.STARTED || rideStatus == RideStatus.FINISHED || rideStatus == RideStatus.CANCELLED) {
            throw new RideBookingCancellationNotAllowedException("Ride cancellation not possible.");
        }

    }

    private void validatePassengerCheckIn(RideBooking rideBooking) {
        if (rideBooking.getBookingStatus() == RideBookingStatus.CANCELLED) {
            throw new PassengerCheckInNotAllowedException("The ride booking is cancelled.");
        }

        RideStatus rideStatus = rideBooking.getRide().getStatus();
        if(rideStatus == RideStatus.STARTED || rideStatus == RideStatus.FINISHED || rideStatus == RideStatus.CANCELLED) {
            throw new PassengerCheckInNotAllowedException("Passenger check-in not possible.");
        }

    }
}

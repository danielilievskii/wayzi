package mk.ukim.finki.wayzi.service.domain.impl;

import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.ride.RideBooking;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.model.domain.vehicle.Vehicle;
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
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class RideBookingServiceImpl implements RideBookingService {

    private final RideService rideService;
    private final AuthService authService;
    private final RideBookingRepository rideBookingRepository;
    private final RideRepository rideRepository;
    private final QRCodeService qrCodeService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public RideBookingServiceImpl(RideService rideService, AuthService authService, RideBookingRepository rideBookingRepository, RideRepository rideRepository, QRCodeService qrCodeService, HandlerExceptionResolver handlerExceptionResolver) {
        this.rideService = rideService;
        this.authService = authService;
        this.rideBookingRepository = rideBookingRepository;
        this.rideRepository = rideRepository;
        this.qrCodeService = qrCodeService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public RideBooking findByIdAndCheckOwnership(Long rideBookingId) {
        RideBooking rideBooking = rideBookingRepository.findById(rideBookingId)
                .orElseThrow(() -> new RideBookingNotFoundException(rideBookingId));

        User currentUser = authService.getAuthenticatedUser();

        if(!rideBooking.getBooker().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException();
        }

        return rideBooking;
    }

    @Override
    public RideBooking save(RideBooking rideBooking) {
        return rideBookingRepository.save(rideBooking);
    }

    @Override
    public RideBooking bookRide(Long rideId, PaymentMethod paymentMethod, Integer bookedSeats) {
        Ride ride = rideService.findById(rideId);
        StandardUser currentUser = authService.getAuthenticatedStandardUser();

        validateBookingRequest(ride, currentUser, bookedSeats);

        Integer totalPrice = calculateTotalPrice(ride.getPricePerSeat(), bookedSeats);
        LocalDateTime bookingTime = LocalDateTime.now();

        RideBooking rideBooking = new RideBooking();
        rideBooking.setRide(ride);
        rideBooking.setBooker(currentUser);
        rideBooking.setPaymentMethod(paymentMethod);
        rideBooking.setBookingStatus(RideBookingStatus.CONFIRMED);
        rideBooking.setCheckInStatus(CheckInStatus.NOT_CHECKED_IN);
        rideBooking.setBookedSeats(bookedSeats);
        rideBooking.setTotalPrice(totalPrice);
        rideBooking.setBookingTime(bookingTime);

        RideBooking savedBooking = rideBookingRepository.save(rideBooking);
        String bookingUrl = "https://localhost:5173/ride-booking/" + savedBooking.getId() + "/check-in";
        String qrCodeUrl = qrCodeService.generateQRCodeBase64(bookingUrl, 200, 200);
        savedBooking.setQrCodeUrl(qrCodeUrl);

        //TODO: Notify driver of new ride booking and send QR code to passenger

        return rideBookingRepository.save(rideBooking);
    }


    private void validateBookingRequest(Ride ride, StandardUser currentUser, Integer bookedSeats) {
        if (isDriverBookingOwnRide(ride.getDriver(), currentUser)) {
            throw new RideBookingNotAllowedException("You cannot book your own ride.");
        }
        if (bookedSeats > ride.getAvailableSeats()) {
            throw new RideBookingNotAllowedException("You cannot book more seats than are available.");
        }
    }

    private boolean isDriverBookingOwnRide(StandardUser driver, StandardUser booker) {
        return Objects.equals(driver.getId(), booker.getId());
    }

    private Integer calculateTotalPrice(Integer pricePerSeat, Integer bookedSeats) {
        return pricePerSeat * bookedSeats;
    }

    @Override
    public void cancelRideBooking(Long rideBookingId) {
        RideBooking rideBooking = findByIdAndCheckOwnership(rideBookingId);
        validateBookingCancellation(rideBooking);

        Ride ride = rideBooking.getRide();
        updateAvailableSeats(ride, rideBooking.getBookedSeats());

        rideBooking.setBookingStatus(RideBookingStatus.CANCELLED);
        rideBookingRepository.save(rideBooking);

        //TODO: Notify driver or ride booking cancellation

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
}

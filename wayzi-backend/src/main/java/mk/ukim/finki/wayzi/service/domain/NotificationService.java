package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.model.domain.RideBooking;
import mk.ukim.finki.wayzi.model.domain.VerificationToken;
import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.model.domain.User;
import mk.ukim.finki.wayzi.model.dto.MailSendingStatus;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {

    List<CompletableFuture<MailSendingStatus>> sendEmailVerification(VerificationToken token);
    List<CompletableFuture<MailSendingStatus>> notifyUserEmailVerified(User user);
    List<CompletableFuture<MailSendingStatus>> notifyDriverOfNewBooking(RideBooking rideBooking);
    List<CompletableFuture<MailSendingStatus>> notifyDriverOfBookingCancellation(RideBooking rideBooking);
    List<CompletableFuture<MailSendingStatus>> notifyPassengerOfRideStart(RideBooking rideBooking);
    List<CompletableFuture<MailSendingStatus>> notifyPassengerOfRideFinish(RideBooking rideBooking);
    List<CompletableFuture<MailSendingStatus>> notifyPassengerOfRideCancellation(RideBooking rideBooking);
    List<CompletableFuture<MailSendingStatus>> notifyPassengerOfRideConfirmation(RideBooking rideBooking);

}

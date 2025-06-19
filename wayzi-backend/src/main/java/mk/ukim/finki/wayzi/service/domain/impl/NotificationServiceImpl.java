package mk.ukim.finki.wayzi.service.domain.impl;

import mk.ukim.finki.wayzi.model.domain.RideBooking;
import mk.ukim.finki.wayzi.model.domain.VerificationToken;
import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.model.domain.User;
import mk.ukim.finki.wayzi.model.dto.MailSendingStatus;
import mk.ukim.finki.wayzi.service.domain.EmailService;
import mk.ukim.finki.wayzi.service.domain.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Value("${frontend.app.base-url}")
    String frontendBaseUrl;

    private final EmailService emailService;

    public NotificationServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> sendEmailVerification(VerificationToken token) {
        Map<String, Object> model = new HashMap<>();
        model.put("header", "Verify your email address");
        model.put("name", token.getUser().getName());

        String verificationUrl = frontendBaseUrl + "/verify-email?token=" + token.getToken();
        model.put("verificationUrl", verificationUrl);

        model.put("bodyContent", "verify-email");

        String subject = "Wayzi – Verify Your Email Address";
        String template = "layout";

        List<CompletableFuture<MailSendingStatus>> mailStatuses = new ArrayList<>();
        mailStatuses.add(emailService.sendMail(new String[]{token.getUser().getEmail()}, subject, template, model));
        return mailStatuses;
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> notifyUserEmailVerified(User user) {
        Map<String, Object> model = new HashMap<>();
        model.put("header", "Email verified");
        model.put("name", user.getName());

        model.put("bodyContent", "email-verified");

        String subject = "Wayzi – Email Verified";
        String template = "layout";

        List<CompletableFuture<MailSendingStatus>> mailStatuses = new ArrayList<>();
        mailStatuses.add(emailService.sendMail(new String[]{user.getEmail()}, subject, template, model));
        return mailStatuses;
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> notifyDriverOfNewBooking(RideBooking rideBooking) {
        Map<String, Object> model = new HashMap<>();
        model.put("header", "New Booking");
        model.put("driverName", rideBooking.getRide().getDriver().getName());
        model.put("from", rideBooking.getRide().getDepartureLocation().getDisplayName());
        model.put("to", rideBooking.getRide().getArrivalLocation().getDisplayName());
        model.put("departureTime", rideBooking.getRide().getDepartureTime());

        model.put("bodyContent", "notify-driver-new-booking");

        String subject = "Wayzi – New Booking!";
        String template = "layout";

        List<CompletableFuture<MailSendingStatus>> mailStatuses = new ArrayList<>();
        mailStatuses.add(emailService.sendMail(new String[]{rideBooking.getRide().getDriver().getEmail()}, subject, template, model));
        return mailStatuses;
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> notifyDriverOfBookingCancellation(RideBooking rideBooking) {
        Map<String, Object> model = new HashMap<>();
        model.put("header", "Booking Cancellation");
        model.put("driverName", rideBooking.getRide().getDriver().getName());
        model.put("from", rideBooking.getRide().getDepartureLocation().getDisplayName());
        model.put("to", rideBooking.getRide().getArrivalLocation().getDisplayName());
        model.put("departureTime", rideBooking.getRide().getDepartureTime());

        model.put("bodyContent", "notify-driver-booking-cancellation");

        String subject = "Wayzi – Booking Cancellation";
        String template = "layout";

        List<CompletableFuture<MailSendingStatus>> mailStatuses = new ArrayList<>();
        mailStatuses.add(emailService.sendMail(new String[]{rideBooking.getRide().getDriver().getEmail()}, subject, template, model));
        return mailStatuses;
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> notifyPassengerOfRideStart(RideBooking rideBooking) {
        Map<String, Object> model = new HashMap<>();
        model.put("header", "Ride Started");
        model.put("passengerName", rideBooking.getBooker().getName());
        model.put("from", rideBooking.getRide().getDepartureLocation().getDisplayName());
        model.put("to", rideBooking.getRide().getArrivalLocation().getDisplayName());

        model.put("bodyContent", "notify-passenger-ride-start");

        String subject = "Wayzi – Ride Started";
        String template = "layout";

        List<CompletableFuture<MailSendingStatus>> mailStatuses = new ArrayList<>();
        mailStatuses.add(emailService.sendMail(new String[]{rideBooking.getBooker().getEmail()}, subject, template, model));
        return mailStatuses;
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> notifyPassengerOfRideFinish(RideBooking rideBooking) {
        Map<String, Object> model = new HashMap<>();
        model.put("header", "Ride Finished");
        model.put("passengerName", rideBooking.getBooker().getName());
        model.put("from", rideBooking.getRide().getDepartureLocation().getDisplayName());
        model.put("to", rideBooking.getRide().getArrivalLocation().getDisplayName());

        model.put("bodyContent", "notify-passenger-ride-finish");

        String subject = "Wayzi – Ride Finished";
        String template = "layout";

        List<CompletableFuture<MailSendingStatus>> mailStatuses = new ArrayList<>();
        mailStatuses.add(emailService.sendMail(new String[]{rideBooking.getBooker().getEmail()}, subject, template, model));
        return mailStatuses;
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> notifyPassengerOfRideConfirmation(RideBooking rideBooking) {
        Map<String, Object> model = new HashMap<>();
        model.put("header", "Ride Confirmation");
        model.put("passengerName", rideBooking.getBooker().getName());
        model.put("from", rideBooking.getRide().getDepartureLocation().getDisplayName());
        model.put("to", rideBooking.getRide().getArrivalLocation().getDisplayName());
        model.put("departureTime", rideBooking.getRide().getDepartureTime());

        model.put("bodyContent", "notify-passenger-ride-confirmation");

        String subject = "Wayzi – Ride Confirmation";
        String template = "layout";

        List<CompletableFuture<MailSendingStatus>> mailStatuses = new ArrayList<>();
        mailStatuses.add(emailService.sendMail(new String[]{rideBooking.getBooker().getEmail()}, subject, template, model));
        return mailStatuses;
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> notifyPassengerOfRideCancellation(RideBooking rideBooking) {
        Map<String, Object> model = new HashMap<>();
        model.put("header", "Ride Cancellation");
        model.put("passengerName", rideBooking.getBooker().getName());
        model.put("from", rideBooking.getRide().getDepartureLocation().getDisplayName());
        model.put("to", rideBooking.getRide().getArrivalLocation().getDisplayName());
        model.put("departureTime", rideBooking.getRide().getDepartureTime());

        model.put("bodyContent", "notify-passenger-ride-cancellation");

        String subject = "Wayzi – Ride Cancellation";
        String template = "layout";

        List<CompletableFuture<MailSendingStatus>> mailStatuses = new ArrayList<>();
        mailStatuses.add(emailService.sendMail(new String[]{rideBooking.getBooker().getEmail()}, subject, template, model));
        return mailStatuses;
    }

}

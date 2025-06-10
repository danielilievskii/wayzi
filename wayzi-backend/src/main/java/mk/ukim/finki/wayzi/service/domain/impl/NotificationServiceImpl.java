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
        return List.of();
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> notifyDriverOfBookingCancellation(RideBooking rideBooking) {
        return List.of();
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> notifyPassengerOfRideStart(RideBooking rideBooking) {
        return List.of();
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> notifyPassengerOfRideFinish(RideBooking rideBooking) {
        return List.of();
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> notifyPassengerOfRideConfirmation(RideBooking rideBooking) {
        return List.of();
    }

    @Override
    public List<CompletableFuture<MailSendingStatus>> notifyPassengerOfRideCancellation(RideBooking rideBooking) {
        return List.of();
    }

}

package mk.ukim.finki.wayzi.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import mk.ukim.finki.wayzi.model.exception.RideNotFoundException;
import mk.ukim.finki.wayzi.model.exception.RideStopNotFoundException;
import mk.ukim.finki.wayzi.service.application.RideBookingApplicationService;
import mk.ukim.finki.wayzi.web.dto.CreateRideBookingDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerExceptionResolver;

@RestController
@RequestMapping("/api")
public class RideBookingController {

    private final RideBookingApplicationService rideBookingApplicationService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public RideBookingController(RideBookingApplicationService rideBookingApplicationService, HandlerExceptionResolver handlerExceptionResolver) {
        this.rideBookingApplicationService = rideBookingApplicationService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @PostMapping("/rides/{rideId}/book")
    public ResponseEntity<?> bookRide(
            @PathVariable Long rideId,
            @RequestBody CreateRideBookingDto rideBookingDto) {

        return ResponseEntity.ok(rideBookingApplicationService.bookRide(rideId, rideBookingDto));

    }

    @PostMapping("/ride-bookings/{rideBookingId}/cancel")
    public ResponseEntity<?> cancelRideBooking(@PathVariable Long rideBookingId) {
        rideBookingApplicationService.cancelRideBooking(rideBookingId);
        return ResponseEntity.ok().build();
    }
}

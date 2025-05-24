package mk.ukim.finki.wayzi.web.controller;

import mk.ukim.finki.wayzi.service.application.RideBookingApplicationService;
import mk.ukim.finki.wayzi.web.dto.ridebooking.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RideBookingController {

    private final RideBookingApplicationService rideBookingApplicationService;

    public RideBookingController(RideBookingApplicationService rideBookingApplicationService) {
        this.rideBookingApplicationService = rideBookingApplicationService;
    }

    @GetMapping("/rides/bookings")
    public ResponseEntity<RideBookingPageDto> findPage(@ModelAttribute RideBookingFilterDto filterDto) {
        return ResponseEntity.ok(rideBookingApplicationService.findPageForUser(filterDto));
    }

    @PostMapping("/rides/{rideId}/book")
    public ResponseEntity<RideBookingDetailsDto> bookRide(
            @PathVariable Long rideId,
            @RequestBody CreateRideBookingDto rideBookingDto) {

        return ResponseEntity.ok(rideBookingApplicationService.bookRide(rideId, rideBookingDto));

    }

    @PutMapping("/rides/bookings/{rideBookingId}/cancel")
    public ResponseEntity<Void> cancelRideBooking(@PathVariable Long rideBookingId) {
        rideBookingApplicationService.cancelRideBooking(rideBookingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rides/bookings/{rideBookingId}")
    public ResponseEntity<RideBookingDetailsDto> getBookingDetailsForBooker(@PathVariable Long rideBookingId) {
        return ResponseEntity.ok(rideBookingApplicationService.getBookingDetailsForBooker(rideBookingId));
    }

    @GetMapping("/rides/bookings/{rideBookingId}/check-in")
    public ResponseEntity<RideBookingCheckInDto> getBookingCheckInDetailsForDriver(@PathVariable Long rideBookingId) {
        return ResponseEntity.ok(rideBookingApplicationService.getBookingCheckInDetailsForDriver(rideBookingId));
    }

    @PutMapping("/rides/bookings/{rideBookingId}/check-in")
    public ResponseEntity<RideBookingCheckInDto> checkInPassenger(@PathVariable Long rideBookingId) {
        return ResponseEntity.ok(rideBookingApplicationService.checkInPassenger(rideBookingId));
    }
}

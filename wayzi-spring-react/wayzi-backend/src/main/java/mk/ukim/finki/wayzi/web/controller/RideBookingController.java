package mk.ukim.finki.wayzi.web.controller;

import mk.ukim.finki.wayzi.service.application.RideBookingApplicationService;
import mk.ukim.finki.wayzi.web.dto.ridebooking.CreateRideBookingDto;
import mk.ukim.finki.wayzi.web.dto.ridebooking.RideBookingCheckInDto;
import mk.ukim.finki.wayzi.web.dto.ridebooking.RideBookingDetailsDto;
import mk.ukim.finki.wayzi.web.dto.ridebooking.RideBookingFilterDto;
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
    public ResponseEntity<?> findPage(@ModelAttribute RideBookingFilterDto filterDto) {
        return ResponseEntity.ok(rideBookingApplicationService.findPageForUser(filterDto));
    }

    @PostMapping("/rides/{rideId}/book")
    public ResponseEntity<?> bookRide(
            @PathVariable Long rideId,
            @RequestBody CreateRideBookingDto rideBookingDto) {

        return ResponseEntity.ok(rideBookingApplicationService.bookRide(rideId, rideBookingDto));

    }

    @GetMapping("/rides/bookings/{rideBookingId}")
    public ResponseEntity<RideBookingDetailsDto> getBookingDetailsForBooker(@PathVariable Long rideBookingId) {
        return ResponseEntity.ok(rideBookingApplicationService.getBookingDetailsForBooker(rideBookingId));
    }

    @PostMapping("/rides/bookings/{rideBookingId}/cancel")
    public ResponseEntity<?> cancelRideBooking(@PathVariable Long rideBookingId) {
        rideBookingApplicationService.cancelRideBooking(rideBookingId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rides/bookings/{rideBookingId}/check-in")
    public ResponseEntity<RideBookingCheckInDto> getBookingCheckInDetailsForDriver(@PathVariable Long rideBookingId) {
        return ResponseEntity.ok(rideBookingApplicationService.getBookingCheckInDetailsForDriver(rideBookingId));
    }

    @PostMapping("/rides/bookings/{rideBookingId}/check-in")
    public ResponseEntity<RideBookingCheckInDto> checkInPassenger(@PathVariable Long rideBookingId) {
        return ResponseEntity.ok(rideBookingApplicationService.checkInPassenger(rideBookingId));
    }
}

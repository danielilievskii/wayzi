package mk.ukim.finki.wayzi.web.dto.ridebooking;

import mk.ukim.finki.wayzi.model.domain.RideBooking;
import org.springframework.data.domain.Page;

import java.util.List;

public record RideBookingPageDto(
        List<RideBookingDetailsDto> rideBookings,
        int totalPages,
        long totalItems,
        int currentPage
) {

    public static RideBookingPageDto from(
            Page<RideBooking> page
    ) {
        return new RideBookingPageDto(
                RideBookingDetailsDto.from(page.getContent()),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber()
        );
    }
}

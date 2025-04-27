package mk.ukim.finki.wayzi.web.dto;

import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import org.springframework.data.domain.Page;

import java.util.List;

public record RidePageDto(
        List<DisplayRideDto> rides,
        int totalPages,
        long totalItems,
        int currentPage
) {

    public static RidePageDto from(
            Page<Ride> page
    ) {
        return new RidePageDto(
                DisplayRideDto.from(page.getContent()),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber()
        );
    }
}

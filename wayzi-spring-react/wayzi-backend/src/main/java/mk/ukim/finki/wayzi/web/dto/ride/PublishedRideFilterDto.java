package mk.ukim.finki.wayzi.web.dto.ride;

import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;

public record PublishedRideFilterDto(
        RideStatus status,
        Integer pageNum,
        Integer pageSize
)
{
    public PublishedRideFilterDto {
        if (pageNum == null) pageNum = 1;
        if (pageSize == null) pageSize = 10;
    }
}

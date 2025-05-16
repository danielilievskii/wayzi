package mk.ukim.finki.wayzi.web.dto.ridebooking;

import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;

import java.time.LocalDate;

public record RideBookingFilterDto(
        RideBookingStatus status,
        Integer pageNum,
        Integer pageSize
)
{
    public RideBookingFilterDto {
        if (pageNum == null) pageNum = 1;
        if (pageSize == null) pageSize = 10;
    }
}

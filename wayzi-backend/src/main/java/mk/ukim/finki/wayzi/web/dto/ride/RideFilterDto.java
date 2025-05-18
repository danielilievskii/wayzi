package mk.ukim.finki.wayzi.web.dto.ride;

import java.time.LocalDate;

public record RideFilterDto (
        Long departureLocationId,
        Long arrivalLocationId,
        LocalDate date,
        Integer passengersNum,
        Integer pageNum,
        Integer pageSize
)
{
    public RideFilterDto {
        if (pageNum == null) pageNum = 1;
        if (pageSize == null) pageSize = 10;
    }
}

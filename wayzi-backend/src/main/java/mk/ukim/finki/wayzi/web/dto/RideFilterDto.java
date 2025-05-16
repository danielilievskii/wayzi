package mk.ukim.finki.wayzi.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

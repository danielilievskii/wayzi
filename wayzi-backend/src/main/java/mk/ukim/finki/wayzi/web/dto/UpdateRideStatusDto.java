package mk.ukim.finki.wayzi.web.dto;

import mk.ukim.finki.wayzi.model.enumeration.RideStatus;

public record UpdateRideStatusDto(
        Long id,
        RideStatus newStatus
) { }

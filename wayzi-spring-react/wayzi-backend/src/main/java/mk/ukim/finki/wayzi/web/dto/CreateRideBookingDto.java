package mk.ukim.finki.wayzi.web.dto;

import mk.ukim.finki.wayzi.model.enumeration.PaymentMethod;

public record CreateRideBookingDto(
        PaymentMethod paymentMethod,
        Integer bookedSeats
) { }

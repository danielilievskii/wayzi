package mk.ukim.finki.wayzi.web.dto.report;

import mk.ukim.finki.wayzi.model.enumeration.PaymentMethod;
import mk.ukim.finki.wayzi.model.enumeration.ReportType;

public record ReportRequestDto(
       Long rideBookingId,
       String description,
       ReportType reportType
) { }

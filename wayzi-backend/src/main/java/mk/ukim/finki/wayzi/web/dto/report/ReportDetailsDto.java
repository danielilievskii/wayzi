package mk.ukim.finki.wayzi.web.dto.report;

import mk.ukim.finki.wayzi.model.domain.Report;
import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.model.enumeration.ReportType;
import mk.ukim.finki.wayzi.web.dto.location.DisplayLocationDto;
import mk.ukim.finki.wayzi.web.dto.ride.DisplayRideDto;
import mk.ukim.finki.wayzi.web.dto.ride.DisplayRideStopDto;
import mk.ukim.finki.wayzi.web.dto.vehicle.DisplayVehicleDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ReportDetailsDto(
       Long id,
       ReportType reportType,
       String reporterName,
       String reportedName,
       String description,
       LocalDateTime reportedAt,
       List<String> imageUrls,
       Long rideId,
       String departureLocationName,
       String arrivalLocationName

) {
    public static ReportDetailsDto from(Report report) {
        return new ReportDetailsDto(
                report.getId(),
                report.getReportType(),
                report.getReporter().getName(),
                report.getReportedUser().getName(),
                report.getDescription(),
                report.getReportedAt(),
                report.getImages().stream()
                .map(img -> "/reports/" + report.getId() + "/images/" + img.getId())
                .collect(Collectors.toList()),
                report.getRide().getId(),
                report.getRide().getDepartureLocation().getDisplayName(),
                report.getRide().getArrivalLocation().getDisplayName()
        );
    }

    public static List<ReportDetailsDto> from (List<Report> reports) {
        return reports.stream().map(ReportDetailsDto::from).toList();
    }
}

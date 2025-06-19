package mk.ukim.finki.wayzi.web.dto.report;

import mk.ukim.finki.wayzi.model.domain.Report;
import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.web.dto.ride.DisplayRideDto;
import org.springframework.data.domain.Page;

import java.util.List;

public record ReportPageDto(
        List<ReportDetailsDto> rides,
        int totalPages,
        long totalItems,
        int currentPage
) {
    public static ReportPageDto from (Page<Report> page) {
        return new ReportPageDto(
                ReportDetailsDto.from(page.getContent()),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumber()
        );
    }
}



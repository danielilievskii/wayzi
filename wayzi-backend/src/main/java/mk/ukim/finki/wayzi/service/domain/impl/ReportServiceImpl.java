package mk.ukim.finki.wayzi.service.domain.impl;

import jakarta.transaction.Transactional;
import mk.ukim.finki.wayzi.model.domain.*;
import mk.ukim.finki.wayzi.model.enumeration.ReportType;
import mk.ukim.finki.wayzi.model.exception.ResourceNotFoundException;
import mk.ukim.finki.wayzi.repository.ReportImageRepository;
import mk.ukim.finki.wayzi.repository.ReportRepository;
import mk.ukim.finki.wayzi.service.domain.AuthService;
import mk.ukim.finki.wayzi.service.domain.ReportService;
import mk.ukim.finki.wayzi.service.domain.RideBookingService;
import mk.ukim.finki.wayzi.web.dto.report.ReportRequestDto;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static mk.ukim.finki.wayzi.specifications.FieldFilterSpecification.*;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportImageRepository imageRepository;
    private final RideBookingService rideBookingService;
    private final AuthService authService;

    public ReportServiceImpl(ReportRepository reportRepository, ReportImageRepository imageRepository, RideBookingService rideBookingService, AuthService authService) {
        this.reportRepository = reportRepository;
        this.imageRepository = imageRepository;
        this.rideBookingService = rideBookingService;
        this.authService = authService;
    }

    @Override
    public Page<Report> findPage(LocalDate from, LocalDate to, Integer pageNum, Integer pageSize) {
        Specification<Report> specification = Specification.where(null);

        if (from != null) {
            specification = specification.and(greaterThan(Report.class, "reportedAt", from.atStartOfDay()));
        }
        if (to != null) {
            specification = specification.and(lessThan(Report.class, "reportedAt", to.atStartOfDay()));
        }

        return reportRepository.findAll(
                specification,
                PageRequest.of(pageNum - 1, pageSize, Sort.by("reportedAt").ascending())
        );
    }

    @Override
    @Transactional
    public Resource loadImageAsResource(Long id) {
        ReportImage image = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Report image with id: %d was not found.", id)));

        byte[] imageBytes = image.getPhoto();
        if (imageBytes == null || imageBytes.length == 0) {
            throw new ResourceNotFoundException(String.format("Report image with id: %d was not found.", id));
        }

        return new ByteArrayResource(imageBytes);
    }

    @Override
    @Transactional
    public void submitReport(ReportRequestDto reportDto, MultipartFile[] images) {
        User reporter = authService.getAuthenticatedUser();

        RideBooking booking;
        if(reportDto.reportType() == ReportType.PASSENGER_ABSENCE) {
            booking = rideBookingService.findByIdEnsuringDriverOwnership(reportDto.rideBookingId());
        } else {
            booking = rideBookingService.findByIdEnsuringBookerOwnership(reportDto.rideBookingId());
        }

        Ride ride = booking.getRide();
        User reported = reportDto.reportType() == ReportType.PASSENGER_ABSENCE ? booking.getBooker() : ride.getDriver();

        Report report = new Report();
        report.setReportType(reportDto.reportType());
        report.setReporter(reporter);
        report.setReportedUser(reported);
        report.setRide(ride);
        report.setRideBooking(booking);
        report.setDescription(report.getDescription());
        report.setReportedAt(LocalDateTime.now());
        reportRepository.save(report);

        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    try {
                        ReportImage reportImage = new ReportImage();
                        reportImage.setReport(report);
                        reportImage.setPhoto(image.getBytes());
                        imageRepository.save(reportImage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}

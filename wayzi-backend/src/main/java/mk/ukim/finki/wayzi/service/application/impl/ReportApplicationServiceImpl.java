package mk.ukim.finki.wayzi.service.application.impl;

import mk.ukim.finki.wayzi.service.application.ReportApplicationService;
import mk.ukim.finki.wayzi.service.domain.ReportService;
import mk.ukim.finki.wayzi.web.dto.report.ReportPageDto;
import mk.ukim.finki.wayzi.web.dto.report.ReportRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public class ReportApplicationServiceImpl implements ReportApplicationService {

    private final ReportService reportService;

    public ReportApplicationServiceImpl(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public ReportPageDto findPage(LocalDate from, LocalDate to, Integer pageNum, Integer pageSize) {
        return ReportPageDto.from(reportService.findPage(from, to, pageNum, pageSize));
    }

    @Override
    public Resource loadImageAsResource(Long id) {
        return reportService.loadImageAsResource(id);
    }

    @Override
    public void submitReport(ReportRequestDto reportDto, MultipartFile[] images) {
        reportService.submitReport(reportDto, images);
    }
}

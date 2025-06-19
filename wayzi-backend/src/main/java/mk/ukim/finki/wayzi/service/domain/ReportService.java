package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.model.domain.Report;
import mk.ukim.finki.wayzi.model.domain.ReportImage;
import mk.ukim.finki.wayzi.web.dto.report.ReportRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    Page<Report> findPage(LocalDate from, LocalDate to, Integer pageNum, Integer pageSize);
    Resource loadImageAsResource(Long id);

    void submitReport(ReportRequestDto request, MultipartFile[] images);
}

package mk.ukim.finki.wayzi.service.application;

import mk.ukim.finki.wayzi.web.dto.report.ReportPageDto;
import mk.ukim.finki.wayzi.web.dto.report.ReportRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface ReportApplicationService {
    ReportPageDto findPage(LocalDate from, LocalDate to,  Integer pageNum, Integer pageSize);
    Resource loadImageAsResource(Long id);
    void submitReport(ReportRequestDto reportDto, MultipartFile[] images);
}

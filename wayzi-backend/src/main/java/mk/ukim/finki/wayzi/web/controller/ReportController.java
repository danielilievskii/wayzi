package mk.ukim.finki.wayzi.web.controller;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.wayzi.service.application.ReportApplicationService;
import mk.ukim.finki.wayzi.web.dto.report.ReportPageDto;
import mk.ukim.finki.wayzi.web.dto.report.ReportRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportApplicationService reportApplicationService;

    public ReportController(ReportApplicationService reportApplicationService) {
        this.reportApplicationService = reportApplicationService;
    }

    @GetMapping
    public ResponseEntity<ReportPageDto> findPage(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false,defaultValue = "1") Integer pageNum,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.ok(reportApplicationService.findPage(from, to, pageNum, pageSize));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> submitReport(
            @RequestPart("data") ReportRequestDto request,
            @RequestPart(value = "images", required = false) MultipartFile[] images) {

        reportApplicationService.submitReport(request, images);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{reportId}/images/{imageId}")
    public ResponseEntity<Resource> loadImageAsResource(@PathVariable Long imageId) {
        Resource resource = reportApplicationService.loadImageAsResource(imageId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

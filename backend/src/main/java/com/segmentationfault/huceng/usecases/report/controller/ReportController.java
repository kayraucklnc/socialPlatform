package com.segmentationfault.huceng.usecases.report.controller;

import com.segmentationfault.huceng.entity.Report;
import com.segmentationfault.huceng.usecases.report.dto.BanRequest;
import com.segmentationfault.huceng.usecases.report.dto.CreateReportRequest;
import com.segmentationfault.huceng.usecases.report.dto.UpdateReportRequest;
import com.segmentationfault.huceng.usecases.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/api/report")
@RequiredArgsConstructor
@Slf4j
public class ReportController {
    private final ReportService reportService;

    @PostMapping()
    public void createReport(@RequestBody CreateReportRequest request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        reportService.createReportForUser(request.getUsername(), currentUser, request.getReportContent());
    }

    @PutMapping("/{reportId}")
    public void updateReport(
            @PathVariable("reportId") Long reportId,
            @RequestBody UpdateReportRequest request
    ) {
        reportService.updateReportForUser(request.getUpdateContent(), reportId);
    }

    @GetMapping("/{username}")
    public Collection<Report> getReportsForUser(@PathVariable("username") String username) {
        return reportService.getAllReportsForUser(username);
    }

    @GetMapping()
    public Collection<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @DeleteMapping("/{reportId}")
    public void deleteReport(@PathVariable("reportId") Long reportId) {
        reportService.deleteReportForUser(reportId);
    }

    @PostMapping("/ban/{username}")
    public void banUser(
            @PathVariable("username") String username,
            @RequestBody BanRequest request
    ) {
        reportService.banUser(username, request.getIndefinite(), request.getDuration());
    }

    @PostMapping("/unban/{username}")
    public void unbanUser(@PathVariable("username") String username) {
        reportService.unbanUser(username);
    }
}

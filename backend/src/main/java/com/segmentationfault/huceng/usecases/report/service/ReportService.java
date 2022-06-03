package com.segmentationfault.huceng.usecases.report.service;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Report;

import java.util.Collection;

public interface ReportService {

    void createReportForUser(String username, String creator, String reportContent);

    Collection<Report> getAllReportsForUser(String username);

    Collection<Report> getAllReports();

    void updateReportForUser(String adminNoteContent, Long reportId);

    void deleteReportForUser(Long reportId);
    void clearReportsForUser(AppUser user);

    void banUser(String username, Boolean indefinite, Long timeout);

    void unbanUser(String username);
}
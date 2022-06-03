package com.segmentationfault.huceng.usecases.report.dto;

import lombok.Data;

@Data
public class DeleteReportRequest {
    private final String username;
    private final Long reportId;
}

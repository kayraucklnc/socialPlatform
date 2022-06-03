package com.segmentationfault.huceng.usecases.report.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdateReportRequest {
    @JsonProperty("update_content")
    private final String updateContent;

    @JsonCreator
    public UpdateReportRequest(String updateContent) {
        this.updateContent = updateContent;
    }
}

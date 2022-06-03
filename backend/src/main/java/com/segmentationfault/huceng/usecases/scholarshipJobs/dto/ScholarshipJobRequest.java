package com.segmentationfault.huceng.usecases.scholarshipJobs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScholarshipJobRequest {
    private String title;
    private String photoLink;
    private String content;
}

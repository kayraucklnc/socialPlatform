package com.segmentationfault.huceng.usecases.scholarshipJobs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetResumeRequest {
    private String resumeFileName;
    private byte[] resume;
}

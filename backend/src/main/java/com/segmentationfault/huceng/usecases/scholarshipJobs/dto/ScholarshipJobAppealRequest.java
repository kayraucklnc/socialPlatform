package com.segmentationfault.huceng.usecases.scholarshipJobs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ScholarshipJobAppealRequest implements Serializable {
    private String description;
    private String resumeFileName;
    private MultipartFile resume;
}

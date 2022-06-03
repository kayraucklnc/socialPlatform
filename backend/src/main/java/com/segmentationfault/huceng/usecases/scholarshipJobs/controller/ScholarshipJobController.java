package com.segmentationfault.huceng.usecases.scholarshipJobs.controller;

import com.segmentationfault.huceng.entity.ScholarshipJob;
import com.segmentationfault.huceng.startup.StartupService;
import com.segmentationfault.huceng.usecases.scholarshipJobs.dto.ScholarshipJobRequest;
import com.segmentationfault.huceng.usecases.scholarshipJobs.service.ScholarshipJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/api/scholarshipJob") //localhost::api/scholarshipJob
@RequiredArgsConstructor
public class ScholarshipJobController {
    private final StartupService startupService;
    private final ScholarshipJobService scholarshipJobService;

    @PostMapping()
    public void createScholarshipJob(@RequestBody ScholarshipJobRequest scholarshipJobRequest) {
        scholarshipJobService.createScholarshipJob(scholarshipJobRequest);
    }

    @GetMapping()
    public Collection<ScholarshipJob> getAllScholarshipJobs() {
        return scholarshipJobService.getAll();
    }

    @GetMapping("/{scholarshipJobID}")
    public ScholarshipJob getScholarshipJob(@PathVariable Long scholarshipJobID) {
        return scholarshipJobService.getScholarshipJob(scholarshipJobID);
    }

    @PutMapping("/{scholarshipJobID}")
    public void editScholarshipJob(@PathVariable Long scholarshipJobID, @RequestBody ScholarshipJobRequest scholarshipJobRequest) {
        scholarshipJobService.editScholarshipJobById(scholarshipJobID, scholarshipJobRequest);
    }

    @DeleteMapping("/{scholarshipJobID}")
    public void deleteScholarshipJob(@PathVariable Long scholarshipJobID) {
        scholarshipJobService.deleteScholarshipJobById(scholarshipJobID);
    }
}

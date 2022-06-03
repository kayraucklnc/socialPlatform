package com.segmentationfault.huceng.usecases.scholarshipJobs.controller;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.ScholarshipJob;
import com.segmentationfault.huceng.entity.ScholarshipJobAppeal;
import com.segmentationfault.huceng.startup.StartupService;
import com.segmentationfault.huceng.usecases.scholarshipJobs.dto.GetResumeRequest;
import com.segmentationfault.huceng.usecases.scholarshipJobs.dto.GetScholarshipJobAppealRequest;
import com.segmentationfault.huceng.usecases.scholarshipJobs.dto.ScholarshipJobAppealRequest;
import com.segmentationfault.huceng.usecases.scholarshipJobs.service.ScholarshipJobService;
import com.segmentationfault.huceng.util.RoleUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping(path = "/api/scholarshipJob/appeal") //localhost::api/scholarshipJob/appeal
@RequiredArgsConstructor
public class ScholarshipJobAppealController {
    private final StartupService startupService;
    private final ScholarshipJobService scholarshipJobService;

    @PostMapping("/{scholarshipJobID}")
    public void createAppeal(@PathVariable Long scholarshipJobID, @ModelAttribute ScholarshipJobAppealRequest scholarshipJobAppealRequest) {
        ScholarshipJob scholarshipJob = scholarshipJobService.getScholarshipJob(scholarshipJobID);
        scholarshipJobService.createAppeal(scholarshipJob, scholarshipJobAppealRequest);
    }

    @GetMapping("/{appealID}")
    public GetScholarshipJobAppealRequest getAppeal(@PathVariable Long appealID) {
        return scholarshipJobService.convertToDto(scholarshipJobService.getAppeal(appealID));
    }

    @GetMapping("of/{scholarshipJobID}")
    public Collection<GetScholarshipJobAppealRequest> getAppealsOfScholarshipJob(@PathVariable Long scholarshipJobID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = startupService.getUser(auth.getName());

        ScholarshipJob scholarshipJob = scholarshipJobService.getScholarshipJob(scholarshipJobID);
        Collection<ScholarshipJobAppeal> scholarshipJobAppeals;
        if (appUser.equals(scholarshipJob.getAuthor()) || RoleUtil.hasRole(appUser, RoleUtil.ROLE_ADMIN)) {
            scholarshipJobAppeals = scholarshipJobService.getAppealsOfScholarshipJob(scholarshipJobID);
        } else {
            scholarshipJobAppeals = scholarshipJobService.getAppealsOfScholarshipJobByUser(scholarshipJobID, appUser.getId());
        }

        Collection<GetScholarshipJobAppealRequest> scholarshipJobAppealRequests = new ArrayList<>();
        for (ScholarshipJobAppeal scholarshipJobAppeal: scholarshipJobAppeals) {
            scholarshipJobAppealRequests.add(scholarshipJobService.convertToDto(scholarshipJobAppeal));
        }

        return scholarshipJobAppealRequests;
    }

    @GetMapping("resume/{appealID}")
    public GetResumeRequest getResumeOfAppeal(@PathVariable Long appealID) {
        ScholarshipJobAppeal scholarshipJobAppeal = scholarshipJobService.getAppeal(appealID);
        return new GetResumeRequest(scholarshipJobAppeal.getResumeFileName(), scholarshipJobAppeal.getResume());
    }

    @PutMapping("/{appealID}")
    public void editAppeal(@PathVariable Long appealID, @ModelAttribute ScholarshipJobAppealRequest scholarshipJobAppealRequest) {
        scholarshipJobService.editAppeal(appealID, scholarshipJobAppealRequest);
    }

    @DeleteMapping("/{appealID}")
    public void deleteAppeal(@PathVariable Long appealID) {
        scholarshipJobService.deleteAppeal(appealID);
    }
}

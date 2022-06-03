package com.segmentationfault.huceng.usecases.scholarshipJobs.service;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.ScholarshipJob;
import com.segmentationfault.huceng.entity.ScholarshipJobAppeal;
import com.segmentationfault.huceng.entity.repository.AppUserRepository;
import com.segmentationfault.huceng.entity.repository.ScholarshipJobAppealRepository;
import com.segmentationfault.huceng.entity.repository.ScholarshipJobRepository;
import com.segmentationfault.huceng.usecases.scholarshipJobs.dto.GetScholarshipJobAppealRequest;
import com.segmentationfault.huceng.usecases.scholarshipJobs.dto.ScholarshipJobAppealRequest;
import com.segmentationfault.huceng.usecases.scholarshipJobs.dto.ScholarshipJobRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScholarshipJobServiceImpl implements ScholarshipJobService {
    private final AppUserRepository appUserRepository;
    private final ScholarshipJobRepository scholarshipJobRepository;
    private final ScholarshipJobAppealRepository scholarshipJobAppealRepository;

    @Override
    public ScholarshipJob getScholarshipJob(Long id) {
        return scholarshipJobRepository.getById(id);
    }

    @Override
    public Collection<ScholarshipJobAppeal> getAppealsOfScholarshipJob(Long scholarshipJobID) {
        return scholarshipJobRepository.getById(scholarshipJobID).getAppeals();
    }

    @Override
    public Collection<ScholarshipJobAppeal> getAppealsOfScholarshipJobByUser(Long scholarshipJobID, Long userID) {
        return scholarshipJobAppealRepository.findAllByParentScholarshipJobAndApplicant(
                scholarshipJobRepository.getById(scholarshipJobID),
                appUserRepository.findById(userID).orElseThrow()
        );
    }

    @Override
    public void createScholarshipJob(ScholarshipJobRequest scholarshipJobRequest) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = appUserRepository.findAppUserByUsername(userName).orElseThrow();

        ScholarshipJob scholarshipJob = convertToEntity(scholarshipJobRequest);
        scholarshipJob.setTimestamp(new Date(System.currentTimeMillis()));
        scholarshipJob.setAuthor(appUser);
        createScholarshipJob(scholarshipJob);
    }

    @Override
    public void createScholarshipJob(ScholarshipJob scholarshipJob) {
        scholarshipJobRepository.save(scholarshipJob);
    }

    @Override
    public void deleteScholarshipJobById(Long id) {
        scholarshipJobRepository.deleteById(id);
    }

    @Override
    public Collection<ScholarshipJob> getAll() {
        return scholarshipJobRepository.findAllByOrderByTimestampDesc();
    }

    @Override
    public ScholarshipJob editScholarshipJobById(Long id, ScholarshipJobRequest scholarshipJobRequest) {
        ScholarshipJob scholarshipJob = scholarshipJobRepository.getById(id);
        scholarshipJob.setTitle(scholarshipJobRequest.getTitle());
        scholarshipJob.setContent(scholarshipJobRequest.getContent());
        scholarshipJob.setPhotoLink(scholarshipJobRequest.getPhotoLink());

        return scholarshipJob;
    }

    @Override
    public ScholarshipJobAppeal getAppeal(Long appealID) {
        return scholarshipJobAppealRepository.getById(appealID);
    }

    @Override
    public void createAppeal(ScholarshipJob scholarshipJob, ScholarshipJobAppealRequest scholarshipJobAppealRequest) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = appUserRepository.findAppUserByUsername(userName).orElseThrow();

        ScholarshipJobAppeal scholarshipJobAppeal = convertToEntity(scholarshipJobAppealRequest);
        scholarshipJobAppeal.setTimestamp(new Date(System.currentTimeMillis()));
        scholarshipJobAppeal.setParentScholarshipJob(scholarshipJob);
        scholarshipJobAppeal.setApplicant(appUser);

        createAppeal(scholarshipJobAppeal);
    }

    @Override
    public void createAppeal(ScholarshipJobAppeal scholarshipJobAppeal) {
        scholarshipJobAppealRepository.save(scholarshipJobAppeal);
    }

    @Override
    public ScholarshipJobAppeal editAppeal(Long appealID, ScholarshipJobAppealRequest scholarshipJobAppealRequest) {
        ScholarshipJobAppeal scholarshipJobAppeal = scholarshipJobAppealRepository.getById(appealID);
        if (scholarshipJobAppealRequest.getDescription() != null) {
            scholarshipJobAppeal.setDescription(scholarshipJobAppealRequest.getDescription());
        }

        if (scholarshipJobAppealRequest.getResumeFileName() != null) {
            scholarshipJobAppeal.setResumeFileName(scholarshipJobAppealRequest.getResumeFileName());
        }

        if (scholarshipJobAppealRequest.getResume() != null) {
            scholarshipJobAppeal.setResume(scholarshipJobAppeal.getResume());
        }

        return scholarshipJobAppeal;
    }

    @Override
    public void deleteAppeal(Long appealID) {
        scholarshipJobAppealRepository.deleteById(appealID);
    }


    @Override
    public ScholarshipJob convertToEntity(ScholarshipJobRequest scholarshipJobRequest) {
        ScholarshipJob scholarshipJob = new ScholarshipJob(
                scholarshipJobRequest.getTitle(),
                scholarshipJobRequest.getPhotoLink(),
                scholarshipJobRequest.getContent()
        );
        return scholarshipJob;
    }

    @Override
    public ScholarshipJobAppeal convertToEntity(ScholarshipJobAppealRequest scholarshipJobAppealRequest) {
        byte[] resume;
        try {
            resume = scholarshipJobAppealRequest.getResume().getBytes();
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't parse resume.");
        }

        ScholarshipJobAppeal scholarshipJobAppeal = new ScholarshipJobAppeal(
                scholarshipJobAppealRequest.getDescription(),
                scholarshipJobAppealRequest.getResumeFileName(),
                resume
        );
        return scholarshipJobAppeal;
    }

    @Override
    public ScholarshipJobRequest convertToDto(ScholarshipJob scholarshipJob) {
        ScholarshipJobRequest scholarshipJobRequest = new ScholarshipJobRequest(
                scholarshipJob.getTitle(),
                scholarshipJob.getPhotoLink(),
                scholarshipJob.getContent()
        );
        return scholarshipJobRequest;
    }

    @Override
    public GetScholarshipJobAppealRequest convertToDto(ScholarshipJobAppeal scholarshipJobAppeal) {
        GetScholarshipJobAppealRequest scholarshipJobAppealRequest = new GetScholarshipJobAppealRequest(
                scholarshipJobAppeal.getId(),
                scholarshipJobAppeal.getDescription(),
                scholarshipJobAppeal.getResumeFileName(),
                scholarshipJobAppeal.getApplicant()
        );
        return scholarshipJobAppealRequest;
    }

}

package com.segmentationfault.huceng.entity.repository;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.ScholarshipJob;
import com.segmentationfault.huceng.entity.ScholarshipJobAppeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScholarshipJobAppealRepository extends JpaRepository<ScholarshipJobAppeal, Long> {
    List<ScholarshipJobAppeal> findAllByOrderByTimestamp();

    List<ScholarshipJobAppeal> findAllByParentScholarshipJobAndApplicant(ScholarshipJob scholarshipJob, AppUser user);
}

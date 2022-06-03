package com.segmentationfault.huceng.entity.repository;

import com.segmentationfault.huceng.entity.ScholarshipJob;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScholarshipJobRepository extends JpaRepository<ScholarshipJob, Long> {
    @Query("SELECT s FROM ScholarshipJob s WHERE title=?1 AND content=?2 AND user_id=?3")
    Optional<ScholarshipJob> getScholarshipJobByInfo(String title, String content, Long author_id);

    List<ScholarshipJob> findAllByOrderByTimestampDesc();
    List<ScholarshipJob> findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(@Param("title") String title, @Param("content") String content, Pageable pager);
}

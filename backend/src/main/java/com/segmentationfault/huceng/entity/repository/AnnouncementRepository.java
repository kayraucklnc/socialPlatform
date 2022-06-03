package com.segmentationfault.huceng.entity.repository;

import com.segmentationfault.huceng.entity.Announcement;
import com.segmentationfault.huceng.entity.AppUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    @Query("SELECT a FROM Announcement a WHERE title=?1 AND content=?2 AND user_id=?3")
    Optional<Announcement> getAnnouncementByInfo(String title, String content, Long author_id);

    @Query("SELECT a FROM Announcement a ORDER BY a.timestamp desc")
    List<Announcement> getAllAnnouncementsByDate();

    List<Announcement> findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(@Param("title") String title, @Param("content") String content, Pageable pager);

}

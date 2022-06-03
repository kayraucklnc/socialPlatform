package com.segmentationfault.huceng.usecases.announcement.service;

import com.segmentationfault.huceng.entity.Announcement;
import com.segmentationfault.huceng.entity.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    @Override
    public void saveAnnouncement(Announcement announcement) {
        announcementRepository.save(announcement);
    }

    @Override
    public Announcement getAnnouncement(Long announcementID) {
        return announcementRepository.getById(announcementID);
    }

    @Override
    public Announcement getAnnouncement(String title, String content, Long author_id) {
        return announcementRepository.getAnnouncementByInfo(title, content, author_id).orElseThrow();
    }

    @Override
    public List<Announcement> getAll() {
        return announcementRepository.getAllAnnouncementsByDate();
    }

    @Override
    public Announcement editAnnouncement(Long id, String title, String content) {
        Announcement announcement = announcementRepository.getById(id);
        announcement.setTitle(title);
        announcement.setContent(content);
        return announcement;
    }

    @Override
    public void deleteAnnouncement(Long announcementID) {
        announcementRepository.deleteById(announcementID);
    }

}

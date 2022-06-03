package com.segmentationfault.huceng.usecases.announcement.service;

import com.segmentationfault.huceng.entity.Announcement;

import java.util.List;

public interface AnnouncementService {

    void saveAnnouncement(Announcement announcement);
    Announcement getAnnouncement(Long announcementID);
    Announcement getAnnouncement(String title, String content, Long author_id);
    List<Announcement> getAll();
    void deleteAnnouncement(Long announcementID);
    Announcement editAnnouncement(Long id, String title, String content);
}

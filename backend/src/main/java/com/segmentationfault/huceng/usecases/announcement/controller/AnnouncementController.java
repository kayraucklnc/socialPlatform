package com.segmentationfault.huceng.usecases.announcement.controller;

import com.segmentationfault.huceng.entity.Announcement;
import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.exception.PermissionDeniedException;
import com.segmentationfault.huceng.startup.StartupService;
import com.segmentationfault.huceng.usecases.announcement.dto.AnnouncementRequest;
import com.segmentationfault.huceng.usecases.announcement.dto.AnnouncementWithAuthorRequest;
import com.segmentationfault.huceng.usecases.announcement.service.AnnouncementService;
import com.segmentationfault.huceng.util.RoleUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(path = "/api/announcement") //localhost::api/announcement
@RequiredArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;
    private final StartupService startupService;

    @PostMapping("/create") //api/announcement/create
    public void createAnnouncement(@RequestBody AnnouncementRequest announcementRequest) {
        String username =  SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = startupService.getUser(username);

        Announcement announcement = new Announcement(
                null,
                announcementRequest.getTitle(),
                announcementRequest.getContent(),
                new Date(System.currentTimeMillis()),
                appUser
        );
        announcementService.saveAnnouncement(announcement);
    }

    @GetMapping("/{id}") //api/announcement/{id}
    public Announcement getAnnouncement(@PathVariable("id") Long id) {
        // TODO Returns the announcement but also gives an error. Check it or cancel it. Everyone can use this and get any announcement.
        return announcementService.getAnnouncement(id);
    }

    @GetMapping //api/announcement/
    public Announcement getAnnouncement(@RequestBody AnnouncementWithAuthorRequest announcementWithAuthorRequest) {
        return announcementService.getAnnouncement(
                announcementWithAuthorRequest.getTitle(),
                announcementWithAuthorRequest.getContent(),
                announcementWithAuthorRequest.getAuthor_id()
        );
    }

    @PutMapping("edit/{id}") //api/announcement/edit/{id}
    public void editAnnouncement(@PathVariable("id") Long id, @RequestBody AnnouncementRequest announcementRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username =  auth.getName();
        AppUser appUser = startupService.getUser(username);

        Announcement announcement = announcementService.getAnnouncement(id);

        if (!appUser.getRoles().stream().anyMatch(a -> a.getName().equals(RoleUtil.ROLE_ADMIN)) &&
                !Objects.equals(appUser.getId(), announcement.getAuthor().getId())) {
            throw new PermissionDeniedException("User doesn't have permission to edit this announcement.");
        }

        announcementService.editAnnouncement(id, announcementRequest.getTitle(), announcementRequest.getContent());
    }

    @GetMapping("/all")
    public List<Announcement> getAll() {
        return announcementService.getAll();
    }

    @DeleteMapping("/{id}") //api/announcement/{id}
    public void deleteAnnouncement(@PathVariable("id") Long id) {
        String username =  SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = startupService.getUser(username);

        if (!appUser.getRoles().stream().anyMatch(a -> a.getName().equals(RoleUtil.ROLE_ADMIN)) &&
                announcementService.getAnnouncement(id).getAuthor().getId() != appUser.getId()) {
            throw new PermissionDeniedException("User doesn't have permission to delete this announcement.");
        }

        announcementService.deleteAnnouncement(id);
    }
}

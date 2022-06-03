package com.segmentationfault.huceng.usecases.searching.service;

import com.segmentationfault.huceng.entity.Announcement;
import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Post;
import com.segmentationfault.huceng.entity.repository.AnnouncementRepository;
import com.segmentationfault.huceng.entity.repository.AppUserRepository;
import com.segmentationfault.huceng.entity.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SearchingServiceImplementation implements SearchingService{

    final private PostRepository postRepository;
    final private AppUserRepository appUserRepository;
    final private AnnouncementRepository announcementRepository;

    @Override
    public List<Post> searchPostBy(String context, int page, int size) {
        Pageable pager = PageRequest.of(page, size, Sort.by("timestamp"));
        return postRepository.findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrPhotoLinkContainingIgnoreCase(context, context, context, pager);
    }

    @Override
    public List<AppUser> searchUsersBy(String searchTerm, int page, int size) {
        Pageable pager = PageRequest.of(page, size, Sort.by("username"));
        return appUserRepository.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrUsernameContainingIgnoreCase(searchTerm, searchTerm, searchTerm, pager);
    }

    @Override
    public List<Announcement> searchAnnouncementsBy(String searchTerm, int page, int size) {
        Pageable pager = PageRequest.of(page, size, Sort.by("timestamp"));
        return announcementRepository.findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(searchTerm, searchTerm, pager);
    }
}

package com.segmentationfault.huceng.usecases.searching.service;

import com.segmentationfault.huceng.entity.Announcement;
import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Post;

import java.util.List;

public interface SearchingService{

    List<Post> searchPostBy(String context, int page, int size);

    List<AppUser> searchUsersBy(String searchTerm, int page, int size);

    List<Announcement> searchAnnouncementsBy(String searchTerm, int page, int size);
}

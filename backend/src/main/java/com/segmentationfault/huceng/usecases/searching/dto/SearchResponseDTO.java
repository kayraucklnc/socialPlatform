package com.segmentationfault.huceng.usecases.searching.dto;

import com.segmentationfault.huceng.entity.Announcement;
import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponseDTO {
    private List<Post> posts;
    private List<AppUser> appUsers;
    private List<Announcement> announcements;

}

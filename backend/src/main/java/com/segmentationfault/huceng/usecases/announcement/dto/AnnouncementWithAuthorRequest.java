package com.segmentationfault.huceng.usecases.announcement.dto;

import lombok.Data;

@Data
public class AnnouncementWithAuthorRequest {
    private String title;
    private String content;
    private Long author_id;
}


package com.segmentationfault.huceng.usecases.post.dto;

import lombok.Data;

@Data
public class PostRequest {
    String title;
    String photoLink;
    String content;
}

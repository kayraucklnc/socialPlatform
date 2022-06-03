package com.segmentationfault.huceng.usecases.post.service;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Post;
import com.segmentationfault.huceng.usecases.post.dto.LikeStatus;
import com.segmentationfault.huceng.usecases.post.dto.PostRequest;

import java.util.Collection;

public interface PostService {
    Post getPost(Long postID);

    void createPost(Post post);

    void createPost(PostRequest postRequest);

    void deletePostById(Long postID);

    Collection<Post> getUserFeed(AppUser user);

    Collection<Post> returnAllPosts();

    void editPostById(Long valueOf, PostRequest post);

    LikeStatus likePostById(Long postID);

    void likePostWithUser(Long postID, Long userID);
}

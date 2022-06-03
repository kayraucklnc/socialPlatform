package com.segmentationfault.huceng.usecases.post.service;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Post;
import com.segmentationfault.huceng.entity.repository.AppUserRepository;
import com.segmentationfault.huceng.entity.repository.PostRepository;
import com.segmentationfault.huceng.exception.PermissionDeniedException;
import com.segmentationfault.huceng.usecases.post.dto.LikeStatus;
import com.segmentationfault.huceng.usecases.post.dto.PostRequest;
import com.segmentationfault.huceng.util.RoleUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final AppUserRepository appUserRepository;


    @Override
    public Post getPost(Long postID) {
        return postRepository.getById(postID);
    }

    @Override
    public void createPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void createPost(PostRequest postRequest) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = appUserRepository.findAppUserByUsername(userName).orElseThrow();

        Post post = new Post(postRequest.getTitle(), postRequest.getPhotoLink(), postRequest.getContent(), new Date(System.currentTimeMillis()), appUser);
        postRepository.save(post);
    }

    @Override
    public void deletePostById(Long postID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.findAppUserByUsername(auth.getName()).orElseThrow();

        if (!appUser.getRoles().stream().anyMatch(a -> a.getName().equals(RoleUtil.ROLE_ADMIN)) &&
                !Objects.equals(appUser.getId(), postRepository.getById(postID).getAppUser().getId())) {
            throw new PermissionDeniedException("User doesn't have permission to delete this post.");
        }
        postRepository.deleteById(postID);
    }

    @Override
    public Collection<Post> getUserFeed(AppUser user) {
        List<Post> feedPosts = new ArrayList<>(user.getPosts());
        for (AppUser followedUser: user.getFollowedUsers()) {
            feedPosts.addAll(followedUser.getPosts());
        }

        // Reverse sorted by timestamp.
        feedPosts.sort(new Comparator<Post>() {
            @Override
            public int compare(Post u1, Post u2) {
                return u2.getTimestamp().compareTo(u1.getTimestamp());
            }
        });
        return feedPosts;
    }


    @Override
    public Collection<Post> returnAllPosts() {
        return postRepository.getAllPostsByDate();
    }

    @Override
    public void editPostById(Long valueOf, PostRequest post) {
        postRepository.getById(valueOf).setContent(post.getContent());
        postRepository.getById(valueOf).setTitle(post.getTitle());
        postRepository.getById(valueOf).setPhotoLink(post.getPhotoLink());
    }

    @Override
    public LikeStatus likePostById(Long postID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.findAppUserByUsername(auth.getName()).orElseThrow();

        if(postRepository.getById(postID).getLikedByUsers().contains(appUser)){
            postRepository.getById(postID).getLikedByUsers().remove(appUser);
            appUser.getLikedPosts().remove(postRepository.getById(postID));
            return new LikeStatus(false);
        }else {
            postRepository.getById(postID).getLikedByUsers().add(appUser);
            appUser.getLikedPosts().add(postRepository.getById(postID));
            return new LikeStatus(true);
        }

    }

    @Override
    public void likePostWithUser(Long postID, Long userID) {
        AppUser appUser = appUserRepository.findById(userID)
                .orElseThrow();

        if(postRepository.getById(postID).getLikedByUsers().contains(appUser)){
            postRepository.getById(postID).getLikedByUsers().remove(appUser);
            appUser.getLikedPosts().remove(postRepository.getById(postID));
        }else {
            postRepository.getById(postID).getLikedByUsers().add(appUser);
            appUser.getLikedPosts().add(postRepository.getById(postID));
        }
    }
}

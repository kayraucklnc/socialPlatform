package com.segmentationfault.huceng.usecases.post.controller;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Post;
import com.segmentationfault.huceng.exception.PermissionDeniedException;
import com.segmentationfault.huceng.startup.StartupService;
import com.segmentationfault.huceng.usecases.post.dto.LikeStatus;
import com.segmentationfault.huceng.usecases.post.dto.PostRequest;
import com.segmentationfault.huceng.usecases.post.service.PostService;
import com.segmentationfault.huceng.util.RoleUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


@RestController
@RequestMapping(path = "/api/post") //localhost::api/post
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final StartupService startupService;

    @PostMapping()
    public void createNewPost(@RequestBody PostRequest postRequest){
        postService.createPost(postRequest);
    }

    @GetMapping()
    public Collection<Post> returnAllPosts(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = startupService.getUser(auth.getName());

        if (!appUser.getRoles().stream().anyMatch(a -> a.getName().equals(RoleUtil.ROLE_ADMIN))) {
            throw new PermissionDeniedException("User doesn't have permissions to get all posts.");
        }
        return postService.returnAllPosts();
    }

    @GetMapping("/feed")
    public Collection<Post> returnFeed() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = startupService.getUser(auth.getName());
        return postService.getUserFeed(appUser);
    }

    @GetMapping("/{username}")
    public Collection<Post> returnAllFrom(@PathVariable String username){
        return startupService.getUser(username).getPosts().stream().sorted(new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o2.getTimestamp().compareTo(o1.getTimestamp());
            }
        }).toList();
    }

    @DeleteMapping("{postId}")
    public void deletePostById(@PathVariable String postId){
        postService.deletePostById(Long.valueOf(postId));
    }

    @PutMapping("/edit/{postId}")
    public void editPostById(@PathVariable String postId, @RequestBody PostRequest postRequest){
        postService.editPostById(Long.valueOf(postId), postRequest);
    }

    @PostMapping("/like/{postId}")
    public LikeStatus likePostById(@PathVariable String postId){
        return postService.likePostById(Long.valueOf(postId));
    }
}
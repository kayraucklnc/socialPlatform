package com.segmentationfault.huceng.usecases.comment.controller;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Comment;
import com.segmentationfault.huceng.startup.StartupService;
import com.segmentationfault.huceng.usecases.comment.dto.CommentCreateRequest;
import com.segmentationfault.huceng.usecases.comment.dto.CommentEditRequest;
import com.segmentationfault.huceng.usecases.comment.dto.CommentLikeStatus;
import com.segmentationfault.huceng.usecases.comment.service.CommentService;
import com.segmentationfault.huceng.usecases.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/api/comment") //localhost::api/comment
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final StartupService startupService;

    @PostMapping()
    public void createComment(@RequestBody CommentCreateRequest commentCreateRequest) {
        commentService.createComment(commentCreateRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteCommentById(id);
    }

    @PutMapping("/{id}")
    public void editComment(@PathVariable Long id, @RequestBody CommentEditRequest commentEditRequest) {
        commentService.editComment(id, commentEditRequest.getContent(), commentEditRequest.getPhotoLink());
    }

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable Long id) {
        return commentService.getComment(id);
    }

    @GetMapping("/ofPost/{postID}")
    public Collection<Comment> getCommentsOfPost(@PathVariable Long postID) {
        return commentService.getCommentsOfPost(postID);
    }

    @GetMapping("/ofUser/{userID}")
    public Collection<Comment> getCommentsOfUser(@PathVariable Long userID) {
        return commentService.getCommentsOfUser(userID);
    }

    @GetMapping("/liked/{commentID}")
    public CommentLikeStatus getIfUserLikedComment(@PathVariable Long commentID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = startupService.getUser(auth.getName());
        return new CommentLikeStatus(commentService.isLikedByUser(commentID, appUser));
    }

    @PostMapping("/like/{commentID}")
    public CommentLikeStatus likeCommentById(@PathVariable String commentID){
        return commentService.likeCommentById(Long.valueOf(commentID));
    }

}

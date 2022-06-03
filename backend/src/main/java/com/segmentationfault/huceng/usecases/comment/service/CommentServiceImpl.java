package com.segmentationfault.huceng.usecases.comment.service;

import com.segmentationfault.huceng.entity.AppUser;
import com.segmentationfault.huceng.entity.Comment;
import com.segmentationfault.huceng.entity.Post;
import com.segmentationfault.huceng.entity.repository.AppUserRepository;
import com.segmentationfault.huceng.entity.repository.CommentRepository;
import com.segmentationfault.huceng.entity.repository.PostRepository;
import com.segmentationfault.huceng.usecases.comment.dto.CommentCreateRequest;
import com.segmentationfault.huceng.usecases.comment.dto.CommentLikeStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final AppUserRepository appUserRepository;
    private final PostRepository postRepository;


    @Override
    public void createComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void createComment(CommentCreateRequest commentRequest) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser appUser = appUserRepository.findAppUserByUsername(userName).orElseThrow();

        Comment comment = new Comment(
                postRepository.getById(commentRequest.getPostID()),
                commentRequest.getContent(),
                commentRequest.getPhotoLink(),
                new Date(System.currentTimeMillis()),
                appUser
        );

        commentRepository.save(comment);
    }

    @Override
    public Comment getComment(Long commentID) {
        return commentRepository.getById(commentID);
    }

    @Override
    public Collection<Comment> getAllComments() {
        return commentRepository.getAllCommentsByDate();
    }

    @Override
    public Collection<Comment> getCommentsOfPost(Long postID) {
        return commentRepository.getCommentsForPostOrderedByDate(postID);
    }

    @Override
    public Collection<Comment> getCommentsOfUser(Long userID) {
        return commentRepository.getCommentsByUserOrderedByDate(userID);
    }

    @Override
    public Comment editComment(Long commentID, String content, String photoLink) {
        Comment comment = commentRepository.getById(commentID);
        comment.setContent(content);
        comment.setPhotoLink(photoLink);
        return comment;
    }

    @Override
    public void deleteCommentById(Long commentID) {
        commentRepository.deleteById(commentID);
    }

    public boolean isLikedByUser(Long commentID, Long userID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.findById(userID).orElseThrow();

        return isLikedByUser(commentID, appUser);
    }

    @Override
    public boolean isLikedByUser(Long commentID, AppUser user) {
        return commentRepository.getById(commentID).getLikedByUsers().contains(user);
    }

    @Override
    public CommentLikeStatus likeCommentById(Long commentID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.findAppUserByUsername(auth.getName()).orElseThrow();

        if(isLikedByUser(commentID, appUser)){
            commentRepository.getById(commentID).getLikedByUsers().remove(appUser);
            appUser.getLikedComments().remove(commentRepository.getById(commentID));
            return new CommentLikeStatus(false);
        }else {
            commentRepository.getById(commentID).getLikedByUsers().add(appUser);
            appUser.getLikedComments().add(commentRepository.getById(commentID));
            return new CommentLikeStatus(true);
        }
    }

    @Override
    public void likeCommentWithUser(Long commentID, Long userID) {
        AppUser appUser = appUserRepository.findById(userID).orElseThrow();

        if(commentRepository.getById(commentID).getLikedByUsers().contains(appUser)){
            commentRepository.getById(commentID).getLikedByUsers().remove(appUser);
            appUser.getLikedComments().remove(commentRepository.getById(commentID));
        }else {
            commentRepository.getById(commentID).getLikedByUsers().add(appUser);
            appUser.getLikedComments().add(commentRepository.getById(commentID));
        }
    }
}

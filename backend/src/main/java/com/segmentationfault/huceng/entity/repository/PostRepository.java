package com.segmentationfault.huceng.entity.repository;

import com.segmentationfault.huceng.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p ORDER BY p.timestamp desc")
    List<Post> getAllPostsByDate();


    List<Post> findAllByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrPhotoLinkContainingIgnoreCase(@Param("title") String title, @Param("content") String content, @Param("photoLink") String photoLink, Pageable pager);

    // TODO Doesn't work.
    @Query(value = "SELECT p from post p WHERE p.user_id IN (SELECT f.followed_user FROM app_user a JOIN following f ON f.follower_user = a.id WHERE a.id = :user_id ) OR p.user_id = :user_id ORDER BY p.timestamp Desc", nativeQuery = true)
    List<Post> findFeed(@Param("user_id") Long user_id);
}
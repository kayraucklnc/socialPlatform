package com.segmentationfault.huceng.entity.repository;

import com.segmentationfault.huceng.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c ORDER BY c.timestamp desc")
    List<Comment> getAllCommentsByDate();

    @Query(value = "SELECT c FROM Comment c WHERE post_id=?1 ORDER BY c.timestamp ASC")
    List<Comment> getCommentsForPostOrderedByDate(Long post_id);

    @Query(value = "SELECT c FROM Comment c WHERE user_id=?1 ORDER BY c.timestamp ASC")
    List<Comment> getCommentsByUserOrderedByDate(Long user_id);

    @Query(value = "SELECT c FROM Comment c WHERE post_id=?1 AND user_id=?2 ORDER BY c.timestamp ASC")
    List<Comment> getCommentsForPostByUserOrderedByDate(Long post_id, Long user_id);
}

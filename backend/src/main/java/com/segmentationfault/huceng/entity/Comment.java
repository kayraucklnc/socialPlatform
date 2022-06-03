package com.segmentationfault.huceng.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    @JoinColumn(name="post_id")
    private Post parentPost;

    private String photoLink;
    private String content;
    private Date timestamp;


    public Comment(Post parentPost, String content, String photoLink, Date timestamp, AppUser author) {
        this.parentPost = parentPost;
        this.content = content;
        this.photoLink = photoLink;
        this.timestamp = timestamp;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", photoLink='" + photoLink + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", appUser=" + author.getUsername() +
                '}';
    }

    @ManyToMany()
    @JoinTable(
            name = "likedComments",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Collection<AppUser> likedByUsers = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private AppUser author;
}

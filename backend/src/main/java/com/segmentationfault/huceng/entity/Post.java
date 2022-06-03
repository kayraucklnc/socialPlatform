package com.segmentationfault.huceng.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String photoLink;
    private String content;
    private String author;
    private Date timestamp;

    public Post(String title, String photoLink, String content, Date timestamp, AppUser appUser) {
        this.title = title;
        this.content = content;
        this.photoLink = photoLink;
        this.author = appUser.getUsername();
        this.timestamp = timestamp;
        this.appUser = appUser;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", photoLink='" + photoLink + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", timestamp=" + timestamp +
                ", appUser=" + appUser.getUsername() +
                '}';
    }

    @ManyToMany()
    @JoinTable(
            name = "likedPosts",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Collection<AppUser> likedByUsers = new ArrayList<>();

    @OneToMany(mappedBy = "parentPost", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private Collection<Comment> comments;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private AppUser appUser;
}

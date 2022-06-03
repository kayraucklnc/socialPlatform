package com.segmentationfault.huceng.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
public class ScholarshipJob {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String photoLink;
    private String content;
    private Date timestamp;

    public ScholarshipJob(String title, String photoLink, String content) {
        this.title = title;
        this.content = content;
        this.photoLink = photoLink;
    }

    public ScholarshipJob(String title, String photoLink, String content, Date timestamp, AppUser author) {
        this.title = title;
        this.content = content;
        this.photoLink = photoLink;
        this.timestamp = timestamp;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", photoLink='" + photoLink + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", author=" + author.getUsername() +
                '}';
    }

    @OneToMany(mappedBy = "parentScholarshipJob", cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private Collection<ScholarshipJobAppeal> appeals;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private AppUser author;
}

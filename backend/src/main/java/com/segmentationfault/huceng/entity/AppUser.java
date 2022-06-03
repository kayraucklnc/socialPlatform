package com.segmentationfault.huceng.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    @JsonIgnore
    private String password;

    public AppUser(String firstName, String lastName, String email, String username, String password) {
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public AppUser(String firstName, String lastName, String email, String username, String password, Collection<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.EAGER) // load all roles as soon as a user is loaded
    private Collection<Role> roles = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "likedByUsers")
    private Collection<Post> likedPosts = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "likedByUsers")
    private Collection<Comment> likedComments = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private Collection<Announcement> announcements = new ArrayList<>();

    @OneToMany(mappedBy = "appUser", cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private Collection<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = {CascadeType.REMOVE, CascadeType.DETACH, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private Collection<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "appUser")
    private Profile profile;

    @OneToMany(mappedBy = "appUser")
    @JsonIgnore
    private Collection<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private Collection<ScholarshipJob> scholarshipJobs = new ArrayList<>();

    @OneToMany(mappedBy = "applicant", cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JsonIgnore
    private Collection<ScholarshipJobAppeal> scholarshipJobAppeals = new ArrayList<>();


    @ManyToMany(cascade={CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "following",
            joinColumns = @JoinColumn(name = "followerUser"),
            inverseJoinColumns = @JoinColumn(name = "followedUser")
    )
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ToString.Exclude
    private Collection<AppUser> followedUsers = new ArrayList<>();

    @ManyToMany(mappedBy = "followedUsers", cascade={CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @ToString.Exclude
    private Collection<AppUser> followers = new ArrayList<>();

    public AppUser(
            Long id,
            String firstName,
            String lastName,
            String email,
            String username,
            String password,
            Collection<Role> roles
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}

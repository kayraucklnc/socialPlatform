package com.segmentationfault.huceng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Entity to model a user that is not yet approved.
 * New sign-ups will create this model and store it. Admins will be able to fetch this and
 * approve will move this to AppUser, disapprove will delete it.
 */
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER) // load all roles as soon as a user is loaded
    private Collection<Role> roles = new ArrayList<>();
}

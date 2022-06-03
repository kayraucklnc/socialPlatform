package com.segmentationfault.huceng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity to keep track of banned users. From each banned user we keep
 * email and username to prevent login and new account creation with these credentials.
 */
@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Banned {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String username;
    private Boolean indefinite;
    private Date timeout;
}

package com.segmentationfault.huceng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String reportContent;
    private Date timestamp;
    private String adminNotes = "";
    private String creatorUsername;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser appUser;
}

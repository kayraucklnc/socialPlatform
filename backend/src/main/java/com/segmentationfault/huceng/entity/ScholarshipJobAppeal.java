package com.segmentationfault.huceng.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ScholarshipJobAppeal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public ScholarshipJobAppeal(String description, String resumeFileName, byte[] resume) {
        this.description = description;
        this.resumeFileName = resumeFileName;
        this.resume = resume;
    }

    public ScholarshipJobAppeal(String description, String resumeFileName, byte[] resume, Date timestamp, ScholarshipJob parent, AppUser applicant) {
        this.description = description;
        this.resumeFileName = resumeFileName;
        this.resume = resume;
        this.applicant = applicant;
        this.timestamp = timestamp;

        this.parentScholarshipJob = parent;
    }

    @Column(columnDefinition="VARCHAR(256)")
    private String description;

    private String resumeFileName;

    @Lob
    @Column(columnDefinition="bytea")
    @Type(type="org.hibernate.type.BinaryType")
    @ToString.Exclude
    private byte[] resume;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private AppUser applicant;

    private Date timestamp;

    @ManyToOne()
    @JoinColumn(name="scholarship_job_id")
    private ScholarshipJob parentScholarshipJob;
}

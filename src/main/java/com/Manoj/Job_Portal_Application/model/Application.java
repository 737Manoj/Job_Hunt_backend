package com.Manoj.Job_Portal_Application.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "application_details")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName ="id", nullable = false)
    @JsonBackReference
    private  User user;

    @ManyToOne
    @JoinColumn(name = "jobId", referencedColumnName = "jobId", nullable = false)
    @JsonBackReference
    private Job job;

    private String status;

    @Column(name = "resume_url")
    private String resumeUrl;

    @Column(name = "resume_filename")
    private String resumeFilename;

}

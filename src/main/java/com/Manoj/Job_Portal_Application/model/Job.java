package com.Manoj.Job_Portal_Application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_details")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;
    private String title;
    private String description;
    private String location;
    private Double salary;

    private Long employerId;

    @OneToMany(mappedBy = "job")
    @JsonManagedReference
    @JsonIgnore
    private List<Application> applications;
}

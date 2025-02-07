package com.Manoj.Job_Portal_Application.controller;

import com.Manoj.Job_Portal_Application.model.Job;
import com.Manoj.Job_Portal_Application.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    @PreAuthorize("hasAuthority('User')")
    public ResponseEntity<List<Job>> getAllJobs(){
        List<Job> jobs = jobService.allJobs();
        if (!jobs.isEmpty()){
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAuthority('Employer')")
    @PostMapping
    public ResponseEntity<Job> postJob(@RequestBody Job job){
        if(job != null && job.getEmployerId() != null){
            Job createdJob = jobService.createJob(job);
            return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('Employer')")
    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable Long jobId){
        Job job = jobService.jobById(jobId);
        if (job != null){
            return new ResponseEntity<>(job, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('Employer')")
    @PutMapping("/update/{jobId}")
    public ResponseEntity<?> updateOldJob(@RequestBody Job newJob, @PathVariable Long jobId) {
        try {
            Job updatedJob = jobService.updateJob(newJob,jobId);
            return new ResponseEntity<>(updatedJob, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(@RequestParam String keyword){
        System.out.println("searching with " + keyword);
        List<Job> jobs = jobService.searchJobs(keyword);
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Employer')")
    @DeleteMapping("/delete/{jobId}")
    public void removeJob(@PathVariable Long jobId){
        jobService.deleteJob(jobId);
    }


}

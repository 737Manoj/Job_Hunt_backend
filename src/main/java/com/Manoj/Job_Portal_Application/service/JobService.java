package com.Manoj.Job_Portal_Application.service;

import com.Manoj.Job_Portal_Application.model.Job;
import com.Manoj.Job_Portal_Application.model.User;
import com.Manoj.Job_Portal_Application.repo.ApplicationRepository;
import com.Manoj.Job_Portal_Application.repo.JobRepository;
import com.Manoj.Job_Portal_Application.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;


@Service
public class JobService {

    @Autowired
    private JobRepository jobRepo;
    @Autowired
    private ApplicationRepository appRepo;
    @Autowired
    private UserRepository userRepo;

    public List<Job> allJobs() {
        return jobRepo.findAll();
    }


    public Job createJob(Job job) {
        User user = userRepo.findById(job.getEmployerId())
                .orElseThrow(() -> new ResourceAccessException("Employer not found."));
        if(!"Employer".equals(user.getRole())){
            throw new IllegalArgumentException("User is not an employer.");
        }
        return jobRepo.save(job);
    }

    public Job jobById(Long jobId) {
        return jobRepo.findById(jobId).orElse(null);
    }


    public Job updateJob(Job newJob, Long jobId) {
        Job oldJob = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceAccessException("Job not found"));
        oldJob.setTitle(newJob.getTitle() != null && !newJob.getTitle().isEmpty() ? newJob.getTitle() : oldJob.getTitle());
        oldJob.setDescription(newJob.getDescription() != null && !newJob.getDescription().isEmpty() ? newJob.getDescription() : oldJob.getDescription());
        oldJob.setLocation(newJob.getLocation() != null && !newJob.getLocation().isEmpty() ? newJob.getLocation() : oldJob.getLocation());
        oldJob.setSalary(newJob.getSalary() != null && !newJob.getSalary().isNaN() ? newJob.getSalary() : oldJob.getSalary());
        return jobRepo.save(oldJob);
    }


    public void deleteJob(Long jobId) {
        appRepo.deleteById(jobId);
        jobRepo.deleteById(jobId);
    }

    public List<Job> searchJobs(String keyword) {
        return jobRepo.searchJobs(keyword);
    }
}

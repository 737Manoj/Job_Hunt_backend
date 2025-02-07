package com.Manoj.Job_Portal_Application.controller;

import com.Manoj.Job_Portal_Application.model.Application;
import com.Manoj.Job_Portal_Application.model.Job;
import com.Manoj.Job_Portal_Application.model.User;
import com.Manoj.Job_Portal_Application.repo.ApplicationRepository;
import com.Manoj.Job_Portal_Application.repo.JobRepository;
import com.Manoj.Job_Portal_Application.repo.UserRepository;
import com.Manoj.Job_Portal_Application.service.ApplicationService;
import com.Manoj.Job_Portal_Application.service.ResumeStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ResumeStorageService resumeStorageService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JobRepository jobRepository;


    @GetMapping
    public List<Application> getAllApps(){
        return applicationService.fetchAll();
    }

    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/upload-resume")
    public ResponseEntity<Application> createApplication(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobId") Long jobId,
            Principal principal
    ) {
        // Fetch User and Job entities
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        if (user==null){
            throw  new RuntimeException("User not found with username: " + username);
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobId));

        boolean isAlreadyApplied = applicationRepository.existsByUserAndJob(user, job);
        if(isAlreadyApplied){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Upload file to Supabase and get the URL
        String resumeUrl = resumeStorageService.uploadFile(file);

        // Create Application object
        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setResumeUrl(resumeUrl);
        application.setResumeFilename(file.getOriginalFilename());
        application.setStatus("PENDING"); // Default status

        // Save the application
        Application savedApplication = applicationRepository.save(application);

        return ResponseEntity.ok(savedApplication);
    }

    @PreAuthorize("hasAuthority('Admin')")
   @DeleteMapping("/delete/{id}")
   public void removeApplication(@PathVariable Long id){
        applicationService.removeApp(id);
    }



}

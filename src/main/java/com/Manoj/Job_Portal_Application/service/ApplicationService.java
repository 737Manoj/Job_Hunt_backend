package com.Manoj.Job_Portal_Application.service;

import com.Manoj.Job_Portal_Application.model.Application;
import com.Manoj.Job_Portal_Application.model.Job;
import com.Manoj.Job_Portal_Application.model.User;
import com.Manoj.Job_Portal_Application.repo.ApplicationRepository;
import com.Manoj.Job_Portal_Application.repo.JobRepository;
import com.Manoj.Job_Portal_Application.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JobRepository jobRepo;
    @Autowired
    private ResumeStorageService resumeStorageService;

    public List<Application> fetchAll() {
        return applicationRepo.findAll();
    }

    @Transactional
    public Application uploadResume(Long userId, Long jobId, MultipartFile resume) throws IOException {
        User user = userRepo.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found."));
        Job job = jobRepo.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found."));
        Optional<Application> existingApp = applicationRepo.findByUserIdAndJobJobId(userId,jobId);
        if(existingApp.isPresent()){
            throw new IllegalArgumentException("User has already applied to the job.");
        }
        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setStatus("Submitted");
        String resumeUrl = resumeStorageService.uploadFile(resume);
        application.setResumeUrl(resumeUrl);
        application.setResumeFilename(resume.getOriginalFilename());


        return applicationRepo.save(application);
    }

    public void removeApp(Long id) {
        applicationRepo.deleteById(id);
    }
}

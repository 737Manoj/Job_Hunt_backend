package com.Manoj.Job_Portal_Application.repo;

import com.Manoj.Job_Portal_Application.model.Application;
import com.Manoj.Job_Portal_Application.model.Job;
import com.Manoj.Job_Portal_Application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByUserIdAndJobJobId(Long userId, Long jobId);
    List<Application> findByUserId(Long userId);

    boolean existsByUserAndJob(User user, Job job);
}

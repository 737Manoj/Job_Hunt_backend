package com.Manoj.Job_Portal_Application.service;

import com.Manoj.Job_Portal_Application.dto.ApplicationDTO;
import com.Manoj.Job_Portal_Application.dto.UserDetailsDTO;
import com.Manoj.Job_Portal_Application.model.User;
import com.Manoj.Job_Portal_Application.repo.ApplicationRepository;
import com.Manoj.Job_Portal_Application.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private JobService jobService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private  boolean isValidEmail(String email){
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public List<User> allUsers() {
         return userRepo.findAll();
    }

    public User addUser(User user) {
        if(!isValidEmail(user.getEmail())){
            throw new IllegalArgumentException("Invalid email address.");
        }
        if(userRepo.findByEmail(user.getEmail()) != null){
            throw new IllegalArgumentException("User with provided email already exists.");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("User");
       return userRepo.save(user);
    }

    public User updateOldUser(User newUser, Long userId) {
        User oldUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceAccessException("User not found"));
        oldUser.setUsername(newUser.getUsername());
        oldUser.setPassword(encoder.encode(newUser.getPassword()));
        oldUser.setRole(newUser.getRole());
        return userRepo.save(oldUser);
    }


    public void deleteUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        if("Employer".equals(user.getRole())){
            jobService.deleteJob(userId);
            userRepo.deleteById(userId);
        } else if ("User".equals(user.getRole())) {
            applicationService.removeApp(userId);
            userRepo.deleteById(userId);
        }
    }

    public String verify(User user) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()){
            System.out.println(user.getRole());
            return jwtService.generateToken(user.getUsername());

        }
        return "failure";
    }

        public UserDetailsDTO getUserProfile(String username) {
            User user = userRepo.findByUsername(username);
            if (user == null){
                throw new RuntimeException("User with given username not found");
            }
            List<ApplicationDTO> applications = applicationRepository.findByUserId(user.getId())
                    .stream()
                    .map(application -> {
                        ApplicationDTO dto = new ApplicationDTO();
                        dto.setId(application.getId());
                        dto.setJobTitle(application.getJob().getTitle());
                        dto.setStatus(application.getStatus());
                        dto.setResumeUrl(application.getResumeUrl());
                        return dto;
                    })
                    .collect(Collectors.toList());

            UserDetailsDTO userDTO = new UserDetailsDTO();
            userDTO.setUsername(user.getUsername());
            userDTO.setApplications(applications);

            return userDTO;
        }
}

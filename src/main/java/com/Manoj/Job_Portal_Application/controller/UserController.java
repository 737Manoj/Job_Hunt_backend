package com.Manoj.Job_Portal_Application.controller;

import com.Manoj.Job_Portal_Application.dto.UserDetailsDTO;
import com.Manoj.Job_Portal_Application.model.User;
import com.Manoj.Job_Portal_Application.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody User user, HttpServletResponse response){
        String token = userService.verify(user);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<User> addUser( @RequestBody User user){
        User user1 = userService.addUser(user);
        if(user1!= null) {
            return new ResponseEntity<>(user1, HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users= userService.allUsers();
        if (!users.isEmpty()){
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{userId}")
    @PreAuthorize("hasAuthority('Admin')")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long userId){
        User userUpdate= userService.updateOldUser(user, userId);
        if (userUpdate != null ){
            return new ResponseEntity<>(userUpdate, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('User')")
    public ResponseEntity<UserDetailsDTO> getUserProfile(Principal principal){
        String username= principal.getName();
        System.out.println("Authenticated User: " + username);
        UserDetailsDTO userDetailsDTO = userService.getUserProfile(username);
        return ResponseEntity.ok(userDetailsDTO);
    }

    @PreAuthorize("hasAuthority('Admin')")
    @DeleteMapping("/delete/{userId}")
    public void removeUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }
}

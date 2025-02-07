package com.Manoj.Job_Portal_Application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {

    private String username;
    private List<ApplicationDTO> applications;
}

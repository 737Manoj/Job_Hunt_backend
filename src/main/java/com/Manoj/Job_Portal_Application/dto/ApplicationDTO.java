package com.Manoj.Job_Portal_Application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDTO {

    private Long id;
    private String jobTitle;
    private String status;
    private String resumeUrl;

}

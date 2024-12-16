package com.learnboot.universalpetcare.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDto {

    private String firstName;
    private String lastName;
    private String gender;
    private String phoneNumber;
    private String email;
    private LocalDate createdAt;
}

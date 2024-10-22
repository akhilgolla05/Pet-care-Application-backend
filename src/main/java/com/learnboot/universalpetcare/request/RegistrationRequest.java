package com.learnboot.universalpetcare.request;

import lombok.Data;

@Data
public class RegistrationRequest {

    private long id;
    private String firstName;
    private String lastName;
    private String gender;
    private String phoneNumber;
    private String email;
    private String password;
    private String userType;
    private boolean isActive;

    private String specialization;
}

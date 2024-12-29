package com.learnboot.universalpetcare.request;

import com.learnboot.universalpetcare.models.User;
import lombok.Data;

import java.util.Date;

@Data
public class VerificationTokenRequest {

    private String token;
    private Date expirationTime;
    private User user;
}

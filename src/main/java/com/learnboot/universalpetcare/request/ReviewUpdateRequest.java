package com.learnboot.universalpetcare.request;

import lombok.Data;

@Data
public class ReviewUpdateRequest {

    private int stars;
    private String feedback;

}

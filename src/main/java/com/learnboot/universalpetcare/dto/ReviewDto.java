package com.learnboot.universalpetcare.dto;

import lombok.Data;

@Data
public class ReviewDto {

    private long id;
    private String feedback;
    private int stars;
    private long vetId;
    private String vetName;
    private long reviewerId;
    private String reviewerName;
    private byte[] reviewerImage;
    private byte[] vetImage;

}

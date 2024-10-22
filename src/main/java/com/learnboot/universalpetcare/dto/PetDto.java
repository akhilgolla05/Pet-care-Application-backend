package com.learnboot.universalpetcare.dto;

import com.learnboot.universalpetcare.models.Appointment;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class PetDto {
    private long id;
    private String name;
    private int age;
    private String breed;
    private String color;
    private String type;

}

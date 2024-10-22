package com.learnboot.universalpetcare.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learnboot.universalpetcare.enums.AppointmentStatus;
import com.learnboot.universalpetcare.models.Pet;
import com.learnboot.universalpetcare.models.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AppointmentDto {

    private long id;
    private String reason;
    private LocalDate date;
    private LocalTime time;
    private String appointmentNo;
    private LocalDate createdAt;
    private PatientDto patient;
    private AppointmentStatus status;
    private VeterinarianDto veterinarian;
    private List<PetDto> pets = new ArrayList<>();

}

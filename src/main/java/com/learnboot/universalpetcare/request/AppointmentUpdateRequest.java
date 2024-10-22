package com.learnboot.universalpetcare.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentUpdateRequest {
    //@JsonFormat(pattern = "yyyy-MM-dd")
    private String appointmentDate;
    //@JsonFormat(pattern = "HH-mm")
    private String appointmentTime;
    private String reason;
}

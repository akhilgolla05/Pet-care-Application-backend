package com.learnboot.universalpetcare.request;

import com.learnboot.universalpetcare.models.Appointment;
import com.learnboot.universalpetcare.models.Pet;
import lombok.Data;

import java.util.List;

@Data
public class BookAppointmentRequest {

    private Appointment appointment;
    private List<Pet> pets;
}

package com.learnboot.universalpetcare.services.veterinarians;

import com.learnboot.universalpetcare.dto.UserDto;
import com.learnboot.universalpetcare.models.Veterinarian;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IVeterinarianService {

    List<UserDto> getAllVeterinariansWithDetails();

    List<UserDto> findAvailableVetForAppointment(String specialization, LocalDate date, LocalTime time);

    List<Veterinarian> getAllVeterinariansBySpecialization(String specialization);

    List<String> getAllVetSpecializations();
}

package com.learnboot.universalpetcare.services.veterinarians;

import com.learnboot.universalpetcare.dto.UserDto;

import java.util.List;

public interface IVeterinarianService {

    List<UserDto> getAllVeterinariansWithDetails();
}

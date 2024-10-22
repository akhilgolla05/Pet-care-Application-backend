package com.learnboot.universalpetcare.factory;

import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.models.Veterinarian;
import com.learnboot.universalpetcare.repository.VeterinarianRepository;
import com.learnboot.universalpetcare.request.RegistrationRequest;
import com.learnboot.universalpetcare.services.user.UserAttributesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VeterinarianFactory {
    private final VeterinarianRepository veterinarianRepository;
    private final UserAttributesMapper userAttributesMapper;

    public User createVeterinarian(RegistrationRequest request) {

        Veterinarian veterinarian = new Veterinarian();
        userAttributesMapper.setCommonAttributes(request, veterinarian);
        veterinarian.setSpecialization(request.getSpecialization());
        return veterinarianRepository.save(veterinarian);
    }

}

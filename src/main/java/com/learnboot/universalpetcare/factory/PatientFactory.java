package com.learnboot.universalpetcare.factory;

import com.learnboot.universalpetcare.models.Patient;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.repository.PatientRepository;
import com.learnboot.universalpetcare.request.RegistrationRequest;
import com.learnboot.universalpetcare.services.user.UserAttributesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientFactory {

    private final PatientRepository patientRepository;
    private final UserAttributesMapper userAttributesMapper;

    public User createPatient(RegistrationRequest request) {
        Patient patient = new Patient();
        userAttributesMapper.setCommonAttributes(request, patient);
        return patientRepository.save(patient);
    }
}

package com.learnboot.universalpetcare.services;

import com.learnboot.universalpetcare.models.Patient;
import com.learnboot.universalpetcare.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> findAllPatients(){
        return patientRepository.findAll();
    }
}

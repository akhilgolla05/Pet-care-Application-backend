package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}

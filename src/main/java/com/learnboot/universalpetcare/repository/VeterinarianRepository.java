package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.models.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {

    List<Veterinarian> findBySpecialization(String specialization);

    boolean existsBySpecialization(String specialization);
}

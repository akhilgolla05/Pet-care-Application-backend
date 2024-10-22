package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.models.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
}

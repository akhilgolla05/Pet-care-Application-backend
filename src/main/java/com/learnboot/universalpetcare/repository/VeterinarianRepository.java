package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.models.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {

    List<Veterinarian> findBySpecialization(String specialization);

    boolean existsBySpecialization(String specialization);

    @Query("select distinct v.specialization from Veterinarian v")
    List<String> findAllVetSpecialization();

    @Query("select v.specialization as specialization, count(v) as count from Veterinarian v group by v.specialization")
    List<Object[]> countVetsBySpecialization();
}

package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.patient.id =: userId OR r.veterinarian.id =:userId")
    Page<Review> findAllByUserId(@Param("userId") long userId, Pageable pageable);

    List<Review> findByVeterinarianId(long veterinarianId);

    //@Query("SELECT r FROM Review r where r.veterinarian.id=:veterinarianId AND r.patient.id=:reviewerId")
    Optional<Review> findByVeterinarianIdAndPatientId(Long veterinarianId, Long reviewerId);

    boolean existsByVeterinarianIdAndPatientId(Long reviewerId, Long veterinarianId);
}

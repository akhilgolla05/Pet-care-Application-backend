package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}

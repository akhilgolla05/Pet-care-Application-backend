package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.models.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

}

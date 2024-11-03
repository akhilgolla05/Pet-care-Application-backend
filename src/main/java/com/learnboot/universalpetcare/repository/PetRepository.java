package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.models.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("select distinct p.type from Pet p")
    List<String> getDistinctPetTypes();

    @Query("select distinct p.color from Pet p")
    List<String> getDistinctPetColors();

    @Query("select distinct p.breed from Pet p where p.type = :petType")
    List<String> getDistinctPetBreedsByPetType(@Param("petType") String petType);
}

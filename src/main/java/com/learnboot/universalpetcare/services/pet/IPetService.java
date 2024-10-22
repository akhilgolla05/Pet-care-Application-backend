package com.learnboot.universalpetcare.services.pet;

import com.learnboot.universalpetcare.models.Pet;

import java.util.List;

public interface IPetService {

    List<Pet> savePetForAppointment(List<Pet> pets);
    Pet updatePet(Pet pet, Long id);
    void deletePet(Long id);
//    List<Pet> getAllPets();
    Pet getPetById(Long id);

}

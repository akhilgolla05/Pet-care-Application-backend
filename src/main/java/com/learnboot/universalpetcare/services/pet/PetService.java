package com.learnboot.universalpetcare.services.pet;

import com.learnboot.universalpetcare.exceptions.ResourceNotFoundException;
import com.learnboot.universalpetcare.models.Pet;
import com.learnboot.universalpetcare.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService implements IPetService{

    private final PetRepository petRepository;

    @Override
    public List<Pet> savePetForAppointment(List<Pet> pets) {
        return petRepository.saveAll(pets);
    }

    @Override
    public Pet updatePet(Pet pet, Long id) {
        Pet dbPet = getPetById(id);
        dbPet.setName(pet.getName());
        dbPet.setAge(pet.getAge());
        dbPet.setBreed(pet.getBreed());
        dbPet.setColor(pet.getColor());
        dbPet.setType(pet.getType());
        return petRepository.save(dbPet);
    }

    @Override
    public void deletePet(Long id) {
         petRepository.findById(id)
                .ifPresentOrElse(petRepository::delete, ()->{
                    throw new ResourceNotFoundException("Pet not found");
                });
    }

    @Override
    public Pet getPetById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
    }
}

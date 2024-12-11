package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.models.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    List<Veterinarian> findAllByUserType(String vet);


}

package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}

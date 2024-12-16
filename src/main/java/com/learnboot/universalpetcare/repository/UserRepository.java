package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.models.Veterinarian;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    List<Veterinarian> findAllByUserType(String type);


    long countByUserType(String type);

    @Transactional
    @Modifying
    @Query("update User u set u.isActive = :enabled where u.id = :userId")
    void updateUserEnabledStatus(@Param("userId") Long userId, @Param("enabled") boolean enabled);
}

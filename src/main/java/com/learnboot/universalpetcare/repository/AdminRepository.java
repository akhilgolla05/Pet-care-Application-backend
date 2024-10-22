package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}

package com.learnboot.universalpetcare.repository;

import com.learnboot.universalpetcare.enums.AppointmentStatus;
import com.learnboot.universalpetcare.models.Appointment;
import com.learnboot.universalpetcare.models.User;
import com.learnboot.universalpetcare.models.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Appointment findByAppointmentNo(String appointmentNo);

    //boolean findByVeterinarianIdAndPatientIdAndStatus(Long veterinarianId, Long reviewerId, AppointmentStatus appointmentStatus);

    boolean existsByVeterinarianIdAndPatientIdAndStatus(Long veterinarianId, Long reviewerId, AppointmentStatus appointmentStatus);

    @Query("select a from Appointment a where a.patient.id =:userId or a.veterinarian.id=:userId")
    List<Appointment> findAllByUserId(@Param("userId") long userId);

    List<Appointment> findByVeterinarianAndDate(User veterinarian, LocalDate date);
}

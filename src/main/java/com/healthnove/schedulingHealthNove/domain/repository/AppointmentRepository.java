package com.healthnove.schedulingHealthNove.domain.repository;

import com.healthnove.schedulingHealthNove.domain.enumerated.Status;
import com.healthnove.schedulingHealthNove.domain.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByIdAndStatus(Long id, Status status);

    List<Appointment> findByUserIdAndStatus(Long id, Status status);

    List<Appointment> findByDoctorIdAndStatus(Long id, Status status);

    Boolean existsByUserIdAndDateBetween(Long id, LocalDateTime firstHour, LocalDateTime lastHour);

    Boolean existsByDoctorIdAndDate(Long id, LocalDateTime date);
}

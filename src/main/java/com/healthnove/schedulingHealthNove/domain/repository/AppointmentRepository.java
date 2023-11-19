package com.healthnove.schedulingHealthNove.domain.repository;

import com.healthnove.schedulingHealthNove.domain.enumerated.Status;
import com.healthnove.schedulingHealthNove.domain.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByIdAndStatus(Long id, Status status);

    List<Appointment> findByUserIdAndStatus(Long id, Status status);

    List<Appointment> findByDoctorIdAndStatus(Long id, Status status);

    @Query("SELECT EXISTS (SELECT 1 FROM Appointment a WHERE a.doctor.id = :id AND a.date = :date AND a.status = 'SCHEDULED')")
    boolean existsByDoctorIdAndDate(@Param("id") Long id, @Param("date") LocalDateTime date);

    @Query("SELECT EXISTS (SELECT 1 FROM Appointment a WHERE a.user.id = :userId AND a.status = SCHEDULED AND a.date BETWEEN :firstHour AND :lastHour)")
    boolean existsByUserIdAndDateBetween(@Param("userId") Long userId,  @Param("firstHour") LocalDateTime firstHour, @Param("lastHour") LocalDateTime lastHour);

}

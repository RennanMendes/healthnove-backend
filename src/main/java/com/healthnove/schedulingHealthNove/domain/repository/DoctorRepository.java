package com.healthnove.schedulingHealthNove.domain.repository;

import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import com.healthnove.schedulingHealthNove.domain.model.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByIdAndActiveTrue(Long id);

    Optional<Doctor> findByIdAndActiveTrue(Long id);

    Page<Doctor> findBySpecialityAndActiveTrue(Pageable page, Speciality speciality);

    Page<Doctor> findByActiveTrue(Pageable page);
}

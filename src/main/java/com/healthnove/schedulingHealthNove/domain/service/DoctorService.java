package com.healthnove.schedulingHealthNove.domain.service;

import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorUpdateDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import com.healthnove.schedulingHealthNove.domain.exception.DoctorAlreadyRegisteredException;
import com.healthnove.schedulingHealthNove.domain.exception.DoctorNotFoundException;
import com.healthnove.schedulingHealthNove.domain.model.Doctor;
import com.healthnove.schedulingHealthNove.domain.model.User;
import com.healthnove.schedulingHealthNove.domain.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoctorService {

    private final DoctorRepository repository;
    private final UserService userService;

    @Autowired
    public DoctorService(DoctorRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public Page<DoctorResponseDto> findAll(Pageable page) {
        Page<Doctor> doctors = repository.findByActiveTrue(page);
        return doctors.map(DoctorResponseDto::new);
    }

    public DoctorResponseDto findById(Long id) {
        Doctor doctor = this.findDoctorById(id);
        return new DoctorResponseDto(doctor);
    }

    public Page<DoctorResponseDto> findBySpeciality(Pageable page, Speciality speciality) {
        Page<Doctor> doctors = repository.findBySpecialityAndActiveTrue(page, speciality);
        return doctors.map(DoctorResponseDto::new);
    }

    public DoctorResponseDto create(Long id, DoctorRequestDto requestDto) {
        if (repository.existsByIdAndActiveTrue(id)) {
            throw new DoctorAlreadyRegisteredException();
        }

        User user = userService.setUserAsDoctor(id);

        return new DoctorResponseDto(repository.save(new Doctor(user, requestDto)));
    }

    @Transactional
    public DoctorResponseDto update(Long id, DoctorUpdateDto requestDto) {
        Doctor doctor = this.findDoctorById(id);
        doctor.setSpeciality(requestDto.speciality());
        return new DoctorResponseDto(doctor);
    }

    @Transactional
    public void delete(Long id) {
        userService.setUserAsPatient(id);
        Doctor doctor = this.findDoctorById(id);
        doctor.setActive(false);
    }

    public Doctor findDoctorById(Long id) {
        return repository.findByIdAndActiveTrue(id).orElseThrow(DoctorNotFoundException::new);
    }

}

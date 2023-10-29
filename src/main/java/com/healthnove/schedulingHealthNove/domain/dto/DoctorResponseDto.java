package com.healthnove.schedulingHealthNove.domain.dto;

import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import com.healthnove.schedulingHealthNove.domain.model.Doctor;

public record DoctorResponseDto(
        Long id,
        UserResponseDto user,
        String crm,
        Speciality speciality
) {

    public DoctorResponseDto(Doctor doctor) {
        this(
                doctor.getId(),
                new UserResponseDto(doctor.getUser()),
                doctor.getCrm(),
                doctor.getSpeciality()
        );
    }


}

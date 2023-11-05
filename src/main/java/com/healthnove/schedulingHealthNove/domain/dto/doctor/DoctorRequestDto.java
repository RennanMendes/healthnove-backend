package com.healthnove.schedulingHealthNove.domain.dto.doctor;

import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DoctorRequestDto(
        @NotBlank
        String crm,
        @NotNull
        Speciality speciality
) {
}

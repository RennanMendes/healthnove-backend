package com.healthnove.schedulingHealthNove.domain.dto.doctor;

import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import jakarta.validation.constraints.NotNull;

public record DoctorUpdateDto(@NotNull Speciality speciality) {
}

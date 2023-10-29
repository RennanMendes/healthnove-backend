package com.healthnove.schedulingHealthNove.domain.dto;

import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import jakarta.validation.constraints.NotNull;

public record DoctorUpdateDto(@NotNull Speciality speciality) {
}

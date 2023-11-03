package com.healthnove.schedulingHealthNove.domain.dto.appointment;

import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record AppointmentRequestDto(

        @NotNull
        Speciality speciality,

        @NotNull
        Date appointmentDate,
        Long doctorId,

        @NotNull
        Long userId
) {
}

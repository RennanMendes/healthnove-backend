package com.healthnove.schedulingHealthNove.domain.dto.appointment;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRequestDto(
        @NotNull
        LocalDateTime appointmentDate,

        Long doctorId,

        @NotNull
        Long userId
) {
}

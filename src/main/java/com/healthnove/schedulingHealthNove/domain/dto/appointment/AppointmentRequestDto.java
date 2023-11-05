package com.healthnove.schedulingHealthNove.domain.dto.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentRequestDto(
        @Future
        LocalDateTime appointmentDate,

        Long doctorId,

        @NotNull
        Long userId
) {
}

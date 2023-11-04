package com.healthnove.schedulingHealthNove.domain.dto.appointment;

import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.user.UserResponseDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Status;
import com.healthnove.schedulingHealthNove.domain.model.Appointment;

import java.time.LocalDateTime;

public record AppointmentResponseDto(
    Long id,
    LocalDateTime date,
    Status status,
    DoctorResponseDto doctorDto,
    UserResponseDto userDto

) {
    public AppointmentResponseDto(Appointment appointment) {
        this(
                appointment.getId(),
                appointment.getDate(),
                appointment.getStatus(),
                new DoctorResponseDto(appointment.getDoctor()),
                new UserResponseDto(appointment.getUser())
        );
    }
}

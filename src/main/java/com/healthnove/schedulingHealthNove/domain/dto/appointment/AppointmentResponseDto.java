package com.healthnove.schedulingHealthNove.domain.dto.appointment;

import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.user.UserResponseDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Status;
import com.healthnove.schedulingHealthNove.domain.model.Appointment;

import java.util.Date;

public record AppointmentResponseDto(
    Long id,
    Date date,
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

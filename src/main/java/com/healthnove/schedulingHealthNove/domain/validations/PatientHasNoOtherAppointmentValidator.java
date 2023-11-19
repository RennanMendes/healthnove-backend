package com.healthnove.schedulingHealthNove.domain.validations;

import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;
import com.healthnove.schedulingHealthNove.domain.exception.ExceptionValidation;
import com.healthnove.schedulingHealthNove.domain.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PatientHasNoOtherAppointmentValidator implements AppointmentSchedulingValidator {

    private final AppointmentRepository repository;

    @Autowired
    public PatientHasNoOtherAppointmentValidator(AppointmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(AppointmentRequestDto requestDto) {
        LocalDateTime firstHour = requestDto.appointmentDate().withHour(7);
        LocalDateTime lastHour = requestDto.appointmentDate().withHour(18);

        var hasAnotherAppointment = repository.existsByUserIdAndDateBetween(requestDto.userId(), firstHour, lastHour);

        if (hasAnotherAppointment) {
            throw new ExceptionValidation("O paciente já tem consulta agendada para esse dia!");
        }
    }
}

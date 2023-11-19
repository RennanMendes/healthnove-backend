package com.healthnove.schedulingHealthNove.domain.validations;

import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;
import com.healthnove.schedulingHealthNove.domain.exception.ExceptionValidation;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class AppointmentTimeValidator implements AppointmentSchedulingValidator {

    @Override
    public void validate(AppointmentRequestDto requestDto) {
        LocalDateTime appointmentDate = requestDto.appointmentDate();
        LocalDateTime now = LocalDateTime.now();
        var differenceInMinutes = Duration.between(now, appointmentDate).toMinutes();

        if (differenceInMinutes < 60) {
            throw new ExceptionValidation("A consulta deve ser agendada com pelo menos 1 hora de antecedência!");
        }
    }
}

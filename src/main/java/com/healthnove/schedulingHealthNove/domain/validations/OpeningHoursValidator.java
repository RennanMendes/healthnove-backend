package com.healthnove.schedulingHealthNove.domain.validations;

import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;
import com.healthnove.schedulingHealthNove.domain.exception.ExceptionValidation;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class OpeningHoursValidator implements AppointmentSchedulingValidator {

    @Override
    public void validate(AppointmentRequestDto requestDto) {
        LocalDateTime appointmentDate = requestDto.appointmentDate();

        Boolean isSunday = appointmentDate.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        Boolean beforeOpening = appointmentDate.getHour() < 7;
        Boolean afterClosing = appointmentDate.getHour() > 18;

        if (isSunday || beforeOpening || afterClosing) {
            throw new ExceptionValidation("Agendamento fora do horário de funcionamento!");
        }
    }
}

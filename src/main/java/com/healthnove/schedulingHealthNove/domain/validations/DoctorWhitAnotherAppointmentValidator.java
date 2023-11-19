package com.healthnove.schedulingHealthNove.domain.validations;

import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;
import com.healthnove.schedulingHealthNove.domain.exception.ExceptionValidation;
import com.healthnove.schedulingHealthNove.domain.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DoctorWhitAnotherAppointmentValidator implements AppointmentSchedulingValidator {

    private final AppointmentRepository repository;

    @Autowired
    public DoctorWhitAnotherAppointmentValidator(AppointmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(AppointmentRequestDto requestDto) {
        var hasAnotherAppointment = repository.existsByDoctorIdAndDate(requestDto.doctorId(), requestDto.appointmentDate());

        if (hasAnotherAppointment) {
            throw new ExceptionValidation("Médico já possui outra consulta neste horário!");
        }
    }

}

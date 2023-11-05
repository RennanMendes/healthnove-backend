package com.healthnove.schedulingHealthNove.domain.validations;

import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;

public interface AppointmentSchedulingValidator {

    void validate(AppointmentRequestDto requestDto);
}

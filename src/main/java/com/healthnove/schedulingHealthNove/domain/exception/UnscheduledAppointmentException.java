package com.healthnove.schedulingHealthNove.domain.exception;

public class UnscheduledAppointmentException extends RuntimeException {

    public UnscheduledAppointmentException() {
        super("unscheduled appointment!");
    }
}

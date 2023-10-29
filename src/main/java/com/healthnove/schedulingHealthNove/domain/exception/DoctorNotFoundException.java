package com.healthnove.schedulingHealthNove.domain.exception;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException() {
        super("Doctor not found!");
    }
}

package com.healthnove.schedulingHealthNove.domain.exception;

public class DoctorAlreadyRegisteredException extends RuntimeException {
    public DoctorAlreadyRegisteredException()  {
        super("Médico já cadastrado!");
    }
}

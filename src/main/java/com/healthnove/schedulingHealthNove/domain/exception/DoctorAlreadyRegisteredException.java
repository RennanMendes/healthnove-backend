package com.healthnove.schedulingHealthNove.domain.exception;

public class DoctorAlreadyRegisteredException extends RuntimeException {
    public DoctorAlreadyRegisteredException()  {
        super("Doctor already registered!");
    }
}

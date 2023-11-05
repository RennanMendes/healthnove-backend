package com.healthnove.schedulingHealthNove.domain.validations;

import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;
import com.healthnove.schedulingHealthNove.domain.exception.ExceptionValidation;
import com.healthnove.schedulingHealthNove.domain.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class DoctorWhitAnotherAppointmentValidatorTest {

    private DoctorWhitAnotherAppointmentValidator validator;

    @Mock
    private AppointmentRepository repository;

    @BeforeEach
    void setUp() {
        validator = new DoctorWhitAnotherAppointmentValidator(repository);
    }

    @Test
    void shouldThrowValidationException_WhenDoctorHasAnotherAppointment() {
        AppointmentRequestDto requestDto = createAppointmentRequestDto();
        when(repository.existsByDoctorIdAndDate(requestDto.doctorId(), requestDto.appointmentDate())).thenReturn(true);

        assertThrows(ExceptionValidation.class, () -> validator.validate(requestDto));
    }

    @Test
    void shouldNotThrowValidationException_WhenDoctorHasNoOtherAppointment() {
        AppointmentRequestDto requestDto = createAppointmentRequestDto();
        when(repository.existsByDoctorIdAndDate(requestDto.doctorId(), requestDto.appointmentDate())).thenReturn(false);

        assertDoesNotThrow(() -> validator.validate(requestDto));
    }

    private AppointmentRequestDto createAppointmentRequestDto() {
        return new AppointmentRequestDto(LocalDateTime.now(), 1L, 2L);
    }

}
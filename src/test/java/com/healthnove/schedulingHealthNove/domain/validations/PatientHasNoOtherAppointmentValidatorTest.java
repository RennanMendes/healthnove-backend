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
class PatientHasNoOtherAppointmentValidatorTest {

    private PatientHasNoOtherAppointmentValidator validator;

    @Mock
    private AppointmentRepository repository;

    @BeforeEach
    void setUp() {
        validator = new PatientHasNoOtherAppointmentValidator(repository);
    }

    @Test
    void shouldThrowValidationException_WhenPatientHasAnotherAppointment() {
        AppointmentRequestDto requestDto = createAppointmentRequestDto();
        LocalDateTime firstHour = requestDto.appointmentDate().withHour(7);
        LocalDateTime lastHour = requestDto.appointmentDate().withHour(18);

        when(repository.existsByUserIdAndDateBetween(requestDto.userId(), firstHour, lastHour)).thenReturn(true);

        assertThrows(ExceptionValidation.class, () -> validator.validate(requestDto));
    }

    @Test
    void shouldThrowValidationException_WhenPatientHasNoAnotherAppointment() {
        AppointmentRequestDto requestDto = createAppointmentRequestDto();
        LocalDateTime firstHour = requestDto.appointmentDate().withHour(7);
        LocalDateTime lastHour = requestDto.appointmentDate().withHour(18);

        when(repository.existsByUserIdAndDateBetween(requestDto.userId(), firstHour, lastHour)).thenReturn(false);

        assertDoesNotThrow(() -> validator.validate(requestDto));
    }


    private AppointmentRequestDto createAppointmentRequestDto() {
        return new AppointmentRequestDto(LocalDateTime.now(), 1L, 2L);
    }

}
package com.healthnove.schedulingHealthNove.domain.validations;

import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;
import com.healthnove.schedulingHealthNove.domain.exception.ExceptionValidation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppointmentTimeValidatorTest {

    private final AppointmentTimeValidator validator = new AppointmentTimeValidator();

    @Test
    void shouldThrowExceptionWhenAppointmentIsLessThanOneHourAway() {
        LocalDateTime lessThanOneHourAway = LocalDateTime.now().plusMinutes(59);
        AppointmentRequestDto requestDto = createAppointmentRequestDto(lessThanOneHourAway);


        assertThrows(ExceptionValidation.class, () -> validator.validate(requestDto));
    }

    @Test
    void shouldNotThrowExceptionWhenAppointmentIsMoreThanOneHourAway() {
        LocalDateTime moreThanOneHourAway = LocalDateTime.now().plusHours(2);
        AppointmentRequestDto requestDto = createAppointmentRequestDto(moreThanOneHourAway);

        assertDoesNotThrow(() -> validator.validate(requestDto));
    }

    private AppointmentRequestDto createAppointmentRequestDto(LocalDateTime date) {
        return new AppointmentRequestDto(date, 1L, 2L);
    }

}
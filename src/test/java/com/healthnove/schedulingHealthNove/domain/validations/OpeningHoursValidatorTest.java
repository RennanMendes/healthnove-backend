package com.healthnove.schedulingHealthNove.domain.validations;

import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;
import com.healthnove.schedulingHealthNove.domain.exception.ExceptionValidation;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.junit.jupiter.api.Assertions.*;

class OpeningHoursValidatorTest {

    private OpeningHoursValidator validator = new OpeningHoursValidator();

    @Test
    void shouldThrowValidationException_WhenAppointmentIsAfter19h() {
        LocalDateTime nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(19, 0);
        AppointmentRequestDto requestDto = createAppointmentRequestDto(nextMonday);

        assertThrows(ExceptionValidation.class, () -> validator.validate(requestDto));
    }

    @Test
    void shouldThrowValidationException_WhenAppointmentIsBefore7h() {
        LocalDateTime nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(06, 0);
        AppointmentRequestDto requestDto = createAppointmentRequestDto(nextMonday);

        assertThrows(ExceptionValidation.class, () -> validator.validate(requestDto));
    }

    @Test
    void shouldThrowValidationException_WhenAppointmentIsSunday() {
        LocalDateTime nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).atTime(10, 0);
        AppointmentRequestDto requestDto = createAppointmentRequestDto(nextMonday);

        assertThrows(ExceptionValidation.class, () -> validator.validate(requestDto));
    }

    @Test
    void shouldNotThrowExceptionWhenAppointmentIsWithinOpeningHours() {
        LocalDateTime nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
        AppointmentRequestDto requestDto = createAppointmentRequestDto(nextMonday);

        assertDoesNotThrow(() -> validator.validate(requestDto));
    }

    private AppointmentRequestDto createAppointmentRequestDto(LocalDateTime date) {
        return new AppointmentRequestDto(date, 1L, 2L);
    }

}
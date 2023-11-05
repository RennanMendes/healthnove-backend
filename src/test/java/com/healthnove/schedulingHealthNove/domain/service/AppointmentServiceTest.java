package com.healthnove.schedulingHealthNove.domain.service;

import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorRequestDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Gender;
import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import com.healthnove.schedulingHealthNove.domain.enumerated.Status;
import com.healthnove.schedulingHealthNove.domain.enumerated.UserType;
import com.healthnove.schedulingHealthNove.domain.exception.DoctorNotFoundException;
import com.healthnove.schedulingHealthNove.domain.exception.UserNotFoundException;
import com.healthnove.schedulingHealthNove.domain.model.Appointment;
import com.healthnove.schedulingHealthNove.domain.model.Doctor;
import com.healthnove.schedulingHealthNove.domain.model.User;
import com.healthnove.schedulingHealthNove.domain.repository.AppointmentRepository;
import com.healthnove.schedulingHealthNove.domain.validations.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppointmentServiceTest {

    private static final Long ID = 1L;
    private static final LocalDateTime NEXT_MONDAY = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY))
            .atTime(10, 0);

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private UserService userService;

    @Mock
    private DoctorService doctorService;

    @Mock
    private AppointmentRepository repository;

    @Spy
    private List<AppointmentSchedulingValidator> validators = new ArrayList<>();

    @Test
    void shouldReturnAppointmentResponseDto_whenFoundUserByValidId() {
        List<Appointment> appointments = new ArrayList<>();
        Appointment appointment = createAppointment(createAppointmentRequestDto());
        appointments.add(appointment);
        List<AppointmentResponseDto> expectedResponse = appointments.stream().map(AppointmentResponseDto::new).collect(Collectors.toList());

        when(repository.findByUserIdAndStatus(ID, Status.SCHEDULED)).thenReturn(appointments);

        List<AppointmentResponseDto> appointmentResponse = appointmentService.findByUserId(ID).collect(Collectors.toList());

        assertEquals(expectedResponse, appointmentResponse);
    }

    @Test
    void shouldReturnAppointmentResponseDto_whenFoundByDoctorId() {
        List<Appointment> appointments = new ArrayList<>();
        Appointment appointment = createAppointment(createAppointmentRequestDto());
        appointments.add(appointment);
        List<AppointmentResponseDto> expectedResponse = appointments.stream().map(AppointmentResponseDto::new).collect(Collectors.toList());

        when(repository.findByDoctorIdAndStatus(ID, Status.SCHEDULED)).thenReturn(appointments);

        List<AppointmentResponseDto> appointmentResponse = appointmentService.findByDoctorId(ID).collect(Collectors.toList());

        assertEquals(expectedResponse, appointmentResponse);
    }

    @Test
    void shouldReturnAppointmentResponseDto_whenValidAppointmentSchedule() {
        AppointmentRequestDto requestDto = createAppointmentRequestDto();
        User user = createUser();
        Doctor doctor = createDoctor();
        Appointment appointment = createAppointment(requestDto);
        AppointmentResponseDto expectedResponse = new AppointmentResponseDto(appointment) ;

        when(userService.findByIdAndActiveTrue(user.getId())).thenReturn(user);
        when(doctorService.findDoctorById(doctor.getId())).thenReturn(doctor);
        when(repository.save(appointment)).thenReturn(appointment);

        AppointmentResponseDto appointmentResponse = appointmentService.scheduleAppointment(requestDto);

        assertEquals(expectedResponse, appointmentResponse);
    }

    @Test
    void shouldReturnUserNotFound_whenFoundByInvalidId() {
        AppointmentRequestDto requestDto = createAppointmentRequestDto();

        when(userService.findByIdAndActiveTrue(ID)).thenThrow(new UserNotFoundException());

        assertThrows(UserNotFoundException.class, () -> appointmentService.scheduleAppointment(requestDto));
    }

    @Test
    void shouldReturnDoctorNotFound_whenFoundByInvalidId() {
        AppointmentRequestDto requestDto = createAppointmentRequestDto();
        User user = createUser();

        when(userService.findByIdAndActiveTrue(user.getId())).thenReturn(user);
        when( doctorService.findDoctorById(ID)).thenThrow(new DoctorNotFoundException());

        assertThrows(DoctorNotFoundException.class, () -> appointmentService.scheduleAppointment(requestDto));
    }

    private AppointmentRequestDto createAppointmentRequestDto() {
        return new AppointmentRequestDto(NEXT_MONDAY, ID, ID);
    }

    private Appointment createAppointment(AppointmentRequestDto dto) {
        return new Appointment(dto.appointmentDate(), createUser(), createDoctor());
    }

    private Doctor createDoctor() {
        return new Doctor(
                createUser(),
                new DoctorRequestDto("123456/SP", Speciality.CARDIOLOGY)
        );
    }

    private User createUser() {
        Date date = new Date(2023, Calendar.JULY, 1);
        return new User(ID,
                "Paulo",
                "Silva",
                "259.705.470-50",
                "(11) 7070-7070",
                date,
                Gender.MALE,
                "teste@email.com",
                "1234",
                UserType.PATIENT,
                true);
    }

}
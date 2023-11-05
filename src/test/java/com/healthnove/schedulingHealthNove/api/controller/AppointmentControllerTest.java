package com.healthnove.schedulingHealthNove.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.appointment.AppointmentResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorRequestDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Gender;
import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import com.healthnove.schedulingHealthNove.domain.enumerated.UserType;
import com.healthnove.schedulingHealthNove.domain.exception.UnscheduledAppointmentException;
import com.healthnove.schedulingHealthNove.domain.model.Appointment;
import com.healthnove.schedulingHealthNove.domain.model.Doctor;
import com.healthnove.schedulingHealthNove.domain.model.User;
import com.healthnove.schedulingHealthNove.domain.service.AppointmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class AppointmentControllerTest {

    private static final String BASE_URL = "/appointments";
    private static final Long ID = 1L;

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    public AppointmentControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    void shouldReturnStatus200_WhenFindAppointmentById() throws Exception {
        AppointmentRequestDto requestDto = createAppointmentRequestDto();
        AppointmentResponseDto responseDto = createAppointmentResponseDto(requestDto);

        when(appointmentService.findAppointmentById(ID)).thenReturn(responseDto);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/{id}", ID));
        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
        Assertions.assertEquals(jsonResponse, objectMapper.writeValueAsString(responseDto));
    }

    @Test
    void shouldReturnStatus404_WhenAppointmentNotFound() throws Exception {
        when(appointmentService.findAppointmentById(ID)).thenThrow(new UnscheduledAppointmentException());

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/{id}", ID));

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnAppointments_whenFoundByUserId() throws Exception {
        AppointmentRequestDto requestDto = createAppointmentRequestDto();
        AppointmentResponseDto responseDto = createAppointmentResponseDto(requestDto);
        String expectedResponse = objectMapper.writeValueAsString(List.of(responseDto));

        when(appointmentService.findByUserId(ID)).thenReturn(Stream.of(responseDto));

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/user/{id}", ID));
        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
        Assertions.assertEquals(expectedResponse, jsonResponse);
    }

    @Test
    void shouldReturnAppointments_whenFoundByDoctorId() throws Exception {
        AppointmentRequestDto requestDto = createAppointmentRequestDto();
        AppointmentResponseDto responseDto = createAppointmentResponseDto(requestDto);
        String expectedResponse = objectMapper.writeValueAsString(List.of(responseDto));

        when(appointmentService.findByDoctorId(ID)).thenReturn(Stream.of(responseDto));

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/doctor/{id}", ID));
        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
        Assertions.assertEquals(expectedResponse, jsonResponse);
    }

    @Test
    void shouldReturnStatus200_WhenScheduleAppointmentSuccessfully() throws Exception {
        AppointmentRequestDto requestDto = createAppointmentRequestDto();
        AppointmentResponseDto responseDto = createAppointmentResponseDto(requestDto);

        when(appointmentService.scheduleAppointment(requestDto)).thenReturn(responseDto);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());
        Assertions.assertEquals(objectMapper.writeValueAsString(responseDto), jsonResponse);
    }

    @Test
    void shouldReturnStatus400_WhenScheduleAppointmentWithoutUserId() throws Exception {
        LocalDateTime nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
        AppointmentRequestDto requestDto = new AppointmentRequestDto(nextMonday,ID,null);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());

    }

    @Test
    void shouldReturnStatus400_WhenScheduleAppointmentWithPastDate() throws Exception {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        AppointmentRequestDto requestDto = new AppointmentRequestDto(pastDate,ID,null);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus204_WhenCancelAppointment() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete(BASE_URL + "/{id}", ID));

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), resultActions.andReturn().getResponse().getStatus());
        verify(appointmentService, times(1)).cancelAppointment(ID);
    }

    private AppointmentRequestDto createAppointmentRequestDto() {
        LocalDateTime nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0);
        return new AppointmentRequestDto(nextMonday, ID, ID);
    }

    private AppointmentResponseDto createAppointmentResponseDto(AppointmentRequestDto dto){
        return new AppointmentResponseDto(createAppointment(dto));
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
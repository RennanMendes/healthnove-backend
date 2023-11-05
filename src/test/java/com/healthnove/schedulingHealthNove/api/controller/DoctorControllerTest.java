package com.healthnove.schedulingHealthNove.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorUpdateDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Gender;
import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import com.healthnove.schedulingHealthNove.domain.enumerated.UserType;
import com.healthnove.schedulingHealthNove.domain.exception.DoctorNotFoundException;
import com.healthnove.schedulingHealthNove.domain.model.Doctor;
import com.healthnove.schedulingHealthNove.domain.model.User;
import com.healthnove.schedulingHealthNove.domain.service.DoctorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class DoctorControllerTest {

    private static final String BASE_URL = "/doctors";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private final DoctorService doctorService;

    @Autowired
    DoctorControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, DoctorService doctorService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.doctorService = doctorService;
    }

    @Test
    void shouldReturnStatus200_WhenFindAllDoctors() throws Exception {
        Doctor doctor = createDoctor(doctorRequestDto());
        Page<DoctorResponseDto> pagedResponse = new PageImpl<>(List.of(new DoctorResponseDto(doctor)));

        when(doctorService.findAll(any())).thenReturn(pagedResponse);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL));
        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
        Assertions.assertEquals(objectMapper.writeValueAsString(pagedResponse), jsonResponse);
    }

    @Test
    void shouldReturnStatus200_WhenFindDoctorById() throws Exception {
        Long id = 1L;
        DoctorResponseDto response = new DoctorResponseDto(createDoctor(doctorRequestDto()));

        when(doctorService.findById(id)).thenReturn(response);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/{id}", id));
        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
        Assertions.assertEquals(objectMapper.writeValueAsString(response), jsonResponse);
    }

    @Test
    void shouldReturnStatus404_WhenDoctorNotFound() throws Exception {
        Long id = 1L;
        when(doctorService.findById(id)).thenThrow(new DoctorNotFoundException());

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/{id}", id));

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus200_WhenFindDoctorBySpeciality() throws Exception {
        Speciality speciality = Speciality.CARDIOLOGY;

        Doctor doctor = createDoctor(doctorRequestDto());
        Page<DoctorResponseDto> pagedResponse = new PageImpl<>(List.of(new DoctorResponseDto(doctor)));

        when(doctorService.findBySpeciality(any(), any())).thenReturn(pagedResponse);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/specialties/{speciality}", speciality));
        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
        Assertions.assertEquals(objectMapper.writeValueAsString(pagedResponse), jsonResponse);
    }

    @Test
    void shouldReturnStatus201_WhenDoctorCreated() throws Exception {
        Long id = 1L;
        DoctorRequestDto requestDto = doctorRequestDto();
        DoctorResponseDto responseDto = new DoctorResponseDto(createDoctor(requestDto));

        when(doctorService.create(id, requestDto)).thenReturn(responseDto);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(responseDto), jsonResponse);
        Assertions.assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenDoctorWithoutCrm() throws Exception {
        Long id = 1L;
        DoctorRequestDto requestDto = new DoctorRequestDto("", Speciality.CARDIOLOGY);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenDoctorWithoutSpeciality() throws Exception {
        Long id = 1L;
        DoctorRequestDto requestDto = new DoctorRequestDto("123456/SP", null);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus200_WhenDoctorUpdated() throws Exception {
        Long id = 1L;
        DoctorUpdateDto requestDto = new DoctorUpdateDto(Speciality.DERMATOLOGY);
        DoctorResponseDto responseDto = new DoctorResponseDto(createDoctor(doctorRequestDto()));

        when(doctorService.update(id, requestDto)).thenReturn(responseDto);

        ResultActions resultActions = mockMvc.perform(put(BASE_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(responseDto), jsonResponse);
        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus204_WhenDoctorDeleted() throws Exception {
        Long id = 1L;

        ResultActions resultActions = mockMvc.perform(delete(BASE_URL + "/{id}", id));

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), resultActions.andReturn().getResponse().getStatus());
    }

    private Doctor createDoctor(DoctorRequestDto requestDto) {
        return new Doctor(
                createUser(),
                requestDto
        );
    }

    private DoctorRequestDto doctorRequestDto() {
        return new DoctorRequestDto("123456/SP", Speciality.CARDIOLOGY);
    }

    private User createUser() {
        Date date = new Date(2023, Calendar.JULY, 1);
        return new User(1L,
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
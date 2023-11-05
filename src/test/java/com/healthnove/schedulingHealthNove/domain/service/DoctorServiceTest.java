package com.healthnove.schedulingHealthNove.domain.service;

import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.doctor.DoctorUpdateDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Gender;
import com.healthnove.schedulingHealthNove.domain.enumerated.Speciality;
import com.healthnove.schedulingHealthNove.domain.enumerated.UserType;
import com.healthnove.schedulingHealthNove.domain.exception.DoctorAlreadyRegisteredException;
import com.healthnove.schedulingHealthNove.domain.exception.DoctorNotFoundException;
import com.healthnove.schedulingHealthNove.domain.model.Doctor;
import com.healthnove.schedulingHealthNove.domain.model.User;
import com.healthnove.schedulingHealthNove.domain.repository.DoctorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class DoctorServiceTest {

    private static final Long ID = 1L;

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private DoctorRepository repository;

    @Mock
    private UserService userService;

    @Test
    void shouldReturnDoctorResponseDto_whenFoundAll() {
        Pageable pageable = PageRequest.of(0, 20);

        Doctor doctor = createDoctor();
        Page<Doctor> doctorPage = new PageImpl<>(List.of(doctor));
        Page<DoctorResponseDto> ExpectedPage = new PageImpl<>(List.of(new DoctorResponseDto(doctor)));

        when(repository.findByActiveTrue(any())).thenReturn(doctorPage);

        Page<DoctorResponseDto> pageResponse = doctorService.findAll(pageable);

        Assertions.assertEquals(ExpectedPage, pageResponse);
    }

    @Test
    void shouldReturnDoctorResponseDto_whenFoundByValidId() {
        Doctor doctor = createDoctor();
        DoctorResponseDto expectedResponse = new DoctorResponseDto(doctor);

        when(repository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(doctor));

        DoctorResponseDto doctorResponse = doctorService.findById(ID);

        assertEquals(expectedResponse, doctorResponse);
    }

    @Test
    void shouldReturnDoctorNotFound_whenFoundByInvalidId() {
        DoctorNotFoundException error = Assertions.assertThrows(DoctorNotFoundException.class, () -> doctorService.findById(ID));

        Assertions.assertNotNull(error);
    }

    @Test
    void shouldReturnDoctorResponseDto_whenFoundBySpeciality() {
        Pageable pageable = PageRequest.of(0, 20);

        Doctor doctor = createDoctor();
        Page<Doctor> doctorPage = new PageImpl<>(List.of(doctor));
        Page<DoctorResponseDto> ExpectedPage = new PageImpl<>(List.of(new DoctorResponseDto(doctor)));

        when(repository.findBySpecialityAndActiveTrue(pageable, Speciality.CARDIOLOGY)).thenReturn(doctorPage);

        Page<DoctorResponseDto> pageResponse = doctorService.findBySpeciality(pageable, Speciality.CARDIOLOGY);

        Assertions.assertEquals(ExpectedPage, pageResponse);
    }

    @Test
    void shouldReturnDoctorResponseDto_whenCreateCorrectly() {
        DoctorRequestDto requestDto = doctorRequestDto();
        Doctor doctor = createDoctor();
        User user = createUser();
        DoctorResponseDto expectedResponse = new DoctorResponseDto(doctor);

        when(repository.existsByIdAndActiveTrue(ID)).thenReturn(false);
        when(userService.setUserAsDoctor(ID)).thenReturn(user);
        when(repository.save(any())).thenReturn(doctor);

        DoctorResponseDto response = doctorService.create(ID, requestDto);

        assertEquals(expectedResponse, response);
    }

    @Test
    void shouldReturnDoctorAlreadyRegistered_whenDoctorAlreadyRegistered() {
        DoctorRequestDto requestDto = doctorRequestDto();

        when(repository.existsByIdAndActiveTrue(ID)).thenReturn(true);

        DoctorAlreadyRegisteredException error = assertThrows(DoctorAlreadyRegisteredException.class,
                () -> doctorService.create(ID, requestDto));

        Assertions.assertNotNull(error);
    }

    @Test
    void shouldReturnDoctorResponseDto_whenDoctorUpdateSuccessfully() {
        DoctorUpdateDto requestDto = new DoctorUpdateDto(Speciality.CARDIOLOGY);
        Doctor doctor = createDoctor();
        DoctorResponseDto expectedResponse = new DoctorResponseDto(doctor);

        when(repository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(doctor));

        DoctorResponseDto response = doctorService.update(ID, requestDto);

        assertEquals(expectedResponse, response);
    }

    @Test
    void shouldReturn400_whenDoctorUpdateNotFound() {
        DoctorUpdateDto requestDto = new DoctorUpdateDto(Speciality.CARDIOLOGY);

        DoctorNotFoundException error = assertThrows(DoctorNotFoundException.class,
                () -> doctorService.update(ID, requestDto));

        Assertions.assertNotNull(error);
    }

    private Doctor createDoctor() {
        return new Doctor(
                createUser(),
                doctorRequestDto()
        );
    }

    private DoctorRequestDto doctorRequestDto() {
        return new DoctorRequestDto("123456/SP", Speciality.CARDIOLOGY);
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
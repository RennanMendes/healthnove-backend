package com.healthnove.schedulingHealthNove.domain.service;

import com.healthnove.schedulingHealthNove.domain.dto.user.UserRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.user.UserResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.user.UserUpdateDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Gender;
import com.healthnove.schedulingHealthNove.domain.enumerated.UserType;
import com.healthnove.schedulingHealthNove.domain.exception.UserNotFoundException;
import com.healthnove.schedulingHealthNove.domain.model.User;
import com.healthnove.schedulingHealthNove.domain.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserServiceTest {

    private static final Long ID = 1L;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldReturnUserResponseDto_whenCreateCorrectly() {
        UserRequestDto userRequest = createUserRequestDto();
        User user = createUser();
        UserResponseDto expectedResponse = createUserResponseDto(user);

        when(repository.save(any())).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("$2a$10$J76/zwJqHkFgKFJi8wWJTuwjYDX8U0elOJVGBVNBwGv2to5nggZHa");

        UserResponseDto userResponse = userService.create(userRequest);

        assertEquals(expectedResponse, userResponse);
    }

    @Test
    void shouldReturnUserResponseDto_whenFoundByValidId() {
        User user = createUser();
        UserResponseDto expectedResponse = createUserResponseDto(user);

        when(repository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(user));

        UserResponseDto userResponse = userService.findById(ID);

        assertEquals(expectedResponse, userResponse);
    }

    @Test
    void shouldReturnUserNotFound_whenFoundByInvalidId() {
        UserNotFoundException error = Assertions.assertThrows(UserNotFoundException.class, () -> userService.findById(ID));

        Assertions.assertNotNull(error);
    }

    @Test
    void shouldReturnUserResponseDto_whenFoundAll() {
        Pageable pageable = PageRequest.of(0, 20);

        User user = createUser();
        Page<User> pageUsers = new PageImpl<>(List.of(user));
        Page<UserResponseDto> ExpectedPage = new PageImpl<>(List.of(new UserResponseDto(user)));

        Mockito.when(repository.findByActiveTrue(pageable)).thenReturn(pageUsers);

        Page<UserResponseDto> pageResponse = userService.findAll(pageable);

        Assertions.assertEquals(ExpectedPage, pageResponse);
    }

    @Test
    void shouldReturnUserResponseDto_whenUpdatedUser() {
        UserUpdateDto userUpdateDto = new UserUpdateDto("Paulo", "Silva", "(11) 7070-7070", "teste@email.com");
        User user = createUser();
        UserResponseDto expectedResponse = createUserResponseDto(user);

        when(repository.findByIdAndActiveTrue(ID)).thenReturn(Optional.of(user));

        UserResponseDto response = userService.update(ID, userUpdateDto);

        Assertions.assertEquals(expectedResponse, response);
    }


    private User createUser() {
        Date date = new Date(2023, Calendar.JULY, 1);
        return new User(
                ID,
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

    private UserRequestDto createUserRequestDto() {
        Date date = new Date(2023, Calendar.JULY, 1);
        return new UserRequestDto(
                "Paulo",
                "Silva",
                "259.705.470-50",
                "(11) 7070-7070",
                date,
                Gender.MALE,
                "teste@email.com",
                "1234");
    }

    private UserResponseDto createUserResponseDto(User user) {
        return new UserResponseDto(user);
    }

}
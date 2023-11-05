package com.healthnove.schedulingHealthNove.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthnove.schedulingHealthNove.domain.dto.user.UserRequestDto;
import com.healthnove.schedulingHealthNove.domain.dto.user.UserResponseDto;
import com.healthnove.schedulingHealthNove.domain.dto.user.UserUpdateDto;
import com.healthnove.schedulingHealthNove.domain.enumerated.Gender;
import com.healthnove.schedulingHealthNove.domain.exception.UserNotFoundException;
import com.healthnove.schedulingHealthNove.domain.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class UserControllerTest {

    private static final String BASE_URL = "/users";
    private static final String NAME = "Paulo";
    private static final String CPF = "259.705.470-50";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private final UserService userService;

    @Autowired
    UserControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, UserService userService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Test
    void shouldReturnStatus200_WhenFindAllUsers() throws Exception {
        UserResponseDto user = createUserResponseDto();
        Page<UserResponseDto> pagedResponse = new PageImpl<>(List.of(user));

        when(userService.findAll(any())).thenReturn(pagedResponse);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL));
        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
        Assertions.assertEquals(jsonResponse, objectMapper.writeValueAsString(pagedResponse));
    }

    @Test
    void shouldReturnStatus200_WhenFindUserById() throws Exception {
        Long id = 1L;
        UserResponseDto user = createUserResponseDto();

        when(userService.findById(id)).thenReturn(user);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/{id}", id));
        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
        Assertions.assertEquals(jsonResponse, objectMapper.writeValueAsString(user));
    }

    @Test
    void shouldReturnStatus404_WhenUserNotFound() throws Exception {
        Long id = 1L;
        when(userService.findById(id)).thenThrow(new UserNotFoundException());

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/{id}", id));

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus201_WhenUserCreated() throws Exception {
        UserRequestDto userRequest = createUserRequestDto(NAME, CPF);
        UserResponseDto userResponse = createUserResponseDto();

        when(userService.create(userRequest)).thenReturn(userResponse);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(userResponse), jsonResponse);
        Assertions.assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenUnnamedUser() throws Exception {
        UserRequestDto userRequest = createUserRequestDto("", CPF);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus400_WhenUserWithoutCPF() throws Exception {
        UserRequestDto userRequest = createUserRequestDto(NAME, "");

        ResultActions resultActions = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus200_WhenUserUpdated() throws Exception {
        Long id = 1L;
        UserUpdateDto userRequest = new UserUpdateDto(NAME, "Silva", "(11) 7070-7070", "teste@email.com");
        UserResponseDto userResponse = createUserResponseDto();

        when(userService.update(id, userRequest)).thenReturn(userResponse);

        ResultActions resultActions = mockMvc.perform(put(BASE_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)));

        String jsonResponse = resultActions.andReturn().getResponse().getContentAsString();

        Assertions.assertEquals(objectMapper.writeValueAsString(userResponse), jsonResponse);
        Assertions.assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    void shouldReturnStatus204_WhenUserDeleted() throws Exception {
        Long id = 1L;

        ResultActions resultActions = mockMvc.perform(delete(BASE_URL + "/{id}", id));

        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), resultActions.andReturn().getResponse().getStatus());
    }


    private UserRequestDto createUserRequestDto(String name, String cpf) {
        Date date = new Date(2023, Calendar.JULY, 1);
        return new UserRequestDto(
                name,
                "Silva",
                cpf,
                "(11) 7070-7070",
                date,
                Gender.MALE,
                "teste@email.com",
                "1234");
    }

    private UserResponseDto createUserResponseDto() {
        Date date = new Date(2023, Calendar.JULY, 1);
        return new UserResponseDto(
                1L,
                "Paulo",
                "Silva",
                "259.705.470-50",
                "(11) 7070-7070",
                date,
                Gender.MALE,
                "teste@email.com");
    }

}
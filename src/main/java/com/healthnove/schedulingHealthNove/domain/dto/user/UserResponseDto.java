package com.healthnove.schedulingHealthNove.domain.dto.user;

import com.healthnove.schedulingHealthNove.domain.enumerated.Gender;
import com.healthnove.schedulingHealthNove.domain.model.User;

import java.util.Date;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String cpf,
        String phone,
        Date brithDate,
        Gender gender,
        String email
) {
    public UserResponseDto(User user) {
        this(
         user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getCpf(),
        user.getPhone(),
        user.getBirthDate(),
        user.getGender(),
        user.getEmail()
        );
    }
}

package com.healthnove.schedulingHealthNove.domain.dto.user;

import com.healthnove.schedulingHealthNove.domain.enumerated.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

public record UserRequestDto(

        @NotBlank
        String name,

        @NotBlank
        String lastName,

        @CPF
        String cpf,

        @NotBlank
        String phone,

        @NotNull
        Date birthDate,

        @NotNull
        Gender gender,

        @Email
        String email,

        @NotBlank
        String password

) {
}

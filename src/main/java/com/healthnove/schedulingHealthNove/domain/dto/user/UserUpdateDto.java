package com.healthnove.schedulingHealthNove.domain.dto.user;

public record UserUpdateDto(
        String firstName,
        String lastName,
        String phone,
        String email
) {
}

package com.healthnove.schedulingHealthNove.domain.dto;

public record UserUpdateDto(
        String firstName,
        String lastName,
        String phone,
        String email
) {
}

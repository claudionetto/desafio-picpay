package com.claudionetto.desafiopicpay.dto;

import com.claudionetto.desafiopicpay.domain.user.UserType;

import java.math.BigDecimal;

public record UserCreateDTO(
        String firstName,
        String lastName,
        String email,
        String document,
        String password,
        UserType userType,
        BigDecimal balance
) {
}

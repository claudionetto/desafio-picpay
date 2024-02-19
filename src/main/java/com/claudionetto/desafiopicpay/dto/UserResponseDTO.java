package com.claudionetto.desafiopicpay.dto;

import com.claudionetto.desafiopicpay.domain.user.UserType;

import java.math.BigDecimal;

public record UserResponseDTO (
        String firstName,
        String lastName,
        String email,
        String document,
        UserType userType,
        BigDecimal balance
) {

}

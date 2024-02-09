package com.claudionetto.desafiopicpay.dto;

import com.claudionetto.desafiopicpay.domain.user.User;

import java.math.BigDecimal;

public record TransactionDTO(
        BigDecimal amount,
        Long payer,
        Long payee

) {
}

package com.claudionetto.desafiopicpay.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO (
        Long id,
        UserResponseDTO payee,
        UserResponseDTO payer,
        BigDecimal amount,
        LocalDateTime timeAtTransaction
) {
}

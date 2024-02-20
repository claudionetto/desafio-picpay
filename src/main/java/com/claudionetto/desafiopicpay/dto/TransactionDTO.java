package com.claudionetto.desafiopicpay.dto;

import java.math.BigDecimal;

public record TransactionDTO(
        BigDecimal amount,
        Long payerId,
        Long payeeId

) {
}

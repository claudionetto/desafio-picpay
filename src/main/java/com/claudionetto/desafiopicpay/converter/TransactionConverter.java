package com.claudionetto.desafiopicpay.converter;

import com.claudionetto.desafiopicpay.domain.transaction.Transaction;
import com.claudionetto.desafiopicpay.dto.TransactionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionConverter {

    private final UserConverter userConverter;

    public TransactionResponseDTO toTransactionResponseDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                userConverter.toUserResponseDTO(transaction.getPayee()),
                userConverter.toUserResponseDTO(transaction.getPayer()),
                transaction.getAmount(),
                transaction.getTimeAtTransaction()
        );
    }

}

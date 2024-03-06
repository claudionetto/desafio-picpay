package com.claudionetto.desafiopicpay.controllers;

import com.claudionetto.desafiopicpay.domain.user.UserType;
import com.claudionetto.desafiopicpay.dto.TransactionDTO;
import com.claudionetto.desafiopicpay.dto.TransactionResponseDTO;
import com.claudionetto.desafiopicpay.dto.UserResponseDTO;
import com.claudionetto.desafiopicpay.services.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @InjectMocks
    TransactionController transactionController;

    @Mock
    TransactionService transactionService;

    @Test
    @DisplayName("Should return a ResponseEntity of TransactionResponseDTO with HttpStatus.OK when successful")
    void createTransaction_ShouldReturnResponseEntityOfTransactionResponseDTOWithHttpStatusOK_whenSuccessful() throws Exception {

        var transactionDTO = new TransactionDTO(BigDecimal.valueOf(20), 1L, 2L);

        UserResponseDTO userResponsePayer = new UserResponseDTO("Claudio", "Netto",
                "claudio@gmail.com", "12345678", UserType.COMMON, BigDecimal.valueOf(50));
        UserResponseDTO userResponsePayee = new UserResponseDTO("José", "Netto",
                "jose@gmail.com", "321321321", UserType.MERCHANT, BigDecimal.valueOf(50));
        TransactionResponseDTO transactionResponseDTOMock = new TransactionResponseDTO(1L, userResponsePayee,
                userResponsePayer, new BigDecimal(20), LocalDateTime.now());

        Mockito.when(transactionService.createTransaction(transactionDTO)).thenReturn(transactionResponseDTOMock);

        ResponseEntity<TransactionResponseDTO> responseEntity = transactionController.createTransaction(transactionDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(transactionResponseDTOMock, responseEntity.getBody());
        verify(transactionService).createTransaction(transactionDTO);
        verifyNoMoreInteractions(transactionService);
    }

}
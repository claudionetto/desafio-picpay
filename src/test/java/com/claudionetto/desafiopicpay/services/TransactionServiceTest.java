package com.claudionetto.desafiopicpay.services;

import com.claudionetto.desafiopicpay.converter.TransactionConverter;
import com.claudionetto.desafiopicpay.domain.user.User;
import com.claudionetto.desafiopicpay.domain.user.UserType;
import com.claudionetto.desafiopicpay.dto.TransactionDTO;
import com.claudionetto.desafiopicpay.dto.TransactionResponseDTO;
import com.claudionetto.desafiopicpay.dto.UserResponseDTO;
import com.claudionetto.desafiopicpay.exceptions.UnauthorizedTransactionException;
import com.claudionetto.desafiopicpay.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private TransactionConverter transactionConverter;
    @Mock
    private AuthorizationService authorizationService;
    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("Should create transaction successfully when everything is OK")
    void createTransaction_ValidTransaction_ShouldReturnResponseDTO_WhenSuccessful() {


        User payer = new User(1L, "Claudio", "Netto", "claudio@gmail.com",
                "123123123", "12345678", UserType.COMMON, BigDecimal.valueOf(50));
        User payee = new User(2L, "José", "Netto", "jose@gmail.com",
                "321321321", "12345678", UserType.MERCHANT, BigDecimal.valueOf(50));


        when(userService.findById(1L)).thenReturn(payer);
        when(userService.findById(2L)).thenReturn(payee);

        when(authorizationService.authorizeTransaction()).thenReturn(true);

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(20), 1L, 2L);

        UserResponseDTO userResponsePayer = new UserResponseDTO("Claudio", "Netto", "claudio@gmail.com",
                "12345678", UserType.COMMON, BigDecimal.valueOf(50));
        UserResponseDTO userResponsePayee = new UserResponseDTO("José", "Netto", "jose@gmail.com",
                "321321321", UserType.MERCHANT, BigDecimal.valueOf(50));

        when(transactionConverter.toTransactionResponseDTO(any()))
                .thenReturn(new TransactionResponseDTO(1L, userResponsePayee, userResponsePayer, new BigDecimal(50), LocalDateTime.now()));

        TransactionResponseDTO responseDTO = transactionService.createTransaction(transactionDTO);

        assertNotNull(responseDTO);
        verify(transactionRepository).save(any());
    }

    @Test
    @DisplayName("Should throw a UnauthorizedTransactionException when transaction is unauthorized")
    void createTransaction_ShouldThrowUnauthorizedTransactionException_WhenTransactionIsUnauthorized() {


        User payer = new User(1L, "Claudio", "Netto", "claudio@gmail.com",
                "123123123", "12345678", UserType.COMMON, BigDecimal.valueOf(50));
        User payee = new User(2L, "José", "Netto", "jose@gmail.com",
                "321321321", "12345678", UserType.MERCHANT, BigDecimal.valueOf(50));


        when(userService.findById(1L)).thenReturn(payer);
        when(userService.findById(2L)).thenReturn(payee);

        when(authorizationService.authorizeTransaction()).thenReturn(false);

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(20), 1L, 2L);

        UnauthorizedTransactionException exception = assertThrows(UnauthorizedTransactionException.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });

        Assertions.assertEquals("Transação não autorizada", exception.getMessage());

        verify(transactionRepository, times(0)).save(any());

    }

    @Test
    @DisplayName("Shouldn't send notification when there is a exception")
    void createTransaction_ShouldNotSendNotification_WhenThereIsException() {

        User payer = new User(1L, "Claudio", "Netto", "claudio@gmail.com",
                "123123123", "12345678", UserType.COMMON, BigDecimal.valueOf(50));
        User payee = new User(2L, "José", "Netto", "jose@gmail.com",
                "321321321", "12345678", UserType.MERCHANT, BigDecimal.valueOf(50));

        when(userService.findById(1L)).thenReturn(payer);
        when(userService.findById(2L)).thenReturn(payee);
        when(authorizationService.authorizeTransaction()).thenReturn(true);

        doThrow(new RuntimeException("Simulate exception in updateBalance")).when(userService).updateBalance(any());

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(20), 1L, 2L);

        assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(transactionDTO);
        });

        verifyNoInteractions(notificationService);
    }
}
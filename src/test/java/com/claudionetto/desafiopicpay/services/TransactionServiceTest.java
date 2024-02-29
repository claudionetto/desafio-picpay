package com.claudionetto.desafiopicpay.services;

import com.claudionetto.desafiopicpay.converter.TransactionConverter;
import com.claudionetto.desafiopicpay.domain.user.User;
import com.claudionetto.desafiopicpay.domain.user.UserType;
import com.claudionetto.desafiopicpay.dto.TransactionDTO;
import com.claudionetto.desafiopicpay.dto.TransactionResponseDTO;
import com.claudionetto.desafiopicpay.dto.UserResponseDTO;
import com.claudionetto.desafiopicpay.exceptions.UnauthorizedTransactionException;
import com.claudionetto.desafiopicpay.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
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

    private User payer;
    private User payee;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp(){
        this.payer = new User(1L, "Claudio", "Netto", "claudio@gmail.com",
                "123123123", "12345678", UserType.COMMON, BigDecimal.valueOf(50));
        this.payee = new User(2L, "José", "Netto", "jose@gmail.com",
                "321321321", "12345678", UserType.MERCHANT, BigDecimal.valueOf(50));
        this.transactionDTO = new TransactionDTO(BigDecimal.valueOf(20), 1L, 2L);
    }

    @Test
    @DisplayName("Should create transaction successfully when everything is OK")
    void createTransaction_ValidTransaction_ShouldReturnResponseDTO_WhenSuccessful() {

        when(userService.findById(this.transactionDTO.payerId())).thenReturn(this.payer);
        when(userService.findById(this.transactionDTO.payeeId())).thenReturn(this.payee);
        when(authorizationService.authorizeTransaction()).thenReturn(true);

        UserResponseDTO userResponsePayer = new UserResponseDTO("Claudio", "Netto",
                "claudio@gmail.com", "12345678", UserType.COMMON, BigDecimal.valueOf(50));
        UserResponseDTO userResponsePayee = new UserResponseDTO("José", "Netto",
                "jose@gmail.com", "321321321", UserType.MERCHANT, BigDecimal.valueOf(50));
        TransactionResponseDTO transactionResponseDTOMock = new TransactionResponseDTO(1L, userResponsePayee,
                userResponsePayer, new BigDecimal(20), LocalDateTime.now());

        when(transactionConverter.toTransactionResponseDTO(any()))
                .thenReturn(transactionResponseDTOMock);

        TransactionResponseDTO responseDTO = transactionService.createTransaction(this.transactionDTO);

        assertNotNull(responseDTO);
        assertEquals(responseDTO, transactionResponseDTOMock);
        assertEquals(payer.getBalance(), BigDecimal.valueOf(30));
        assertEquals(payee.getBalance(), BigDecimal.valueOf(70));
        verify(transactionRepository).save(any());
        verify(userService).validateUser(payer, transactionDTO.amount());
        verify(userService, times(2)).findById(any(Long.class));
        verify(userService, times(2)).updateBalance(any(User.class));
    }

    @Test
    @DisplayName("Shouldn't send notification when there is a exception")
    void createTransaction_ShouldNotSendNotification_WhenThereIsException() {

        when(userService.findById(1L)).thenReturn(this.payer);
        when(userService.findById(2L)).thenReturn(this.payee);
        when(authorizationService.authorizeTransaction()).thenReturn(true);

        doThrow(new RuntimeException("Simulate exception in updateBalance")).when(userService).updateBalance(any());

        assertThrows(RuntimeException.class, () -> transactionService.createTransaction(this.transactionDTO));

        verifyNoInteractions(notificationService);
    }
}
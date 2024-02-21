package com.claudionetto.desafiopicpay.services;

import com.claudionetto.desafiopicpay.converter.TransactionConverter;
import com.claudionetto.desafiopicpay.domain.transaction.Transaction;
import com.claudionetto.desafiopicpay.domain.user.User;
import com.claudionetto.desafiopicpay.dto.TransactionDTO;
import com.claudionetto.desafiopicpay.dto.TransactionResponseDTO;
import com.claudionetto.desafiopicpay.exceptions.UnauthorizedTransactionException;
import com.claudionetto.desafiopicpay.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final TransactionConverter transactionConverter;
    private final AuthorizationService authorizationService;

    @Transactional
    public TransactionResponseDTO createTransaction(TransactionDTO transactionDTO) {

        User payer = this.userService.findById(transactionDTO.payerId());
        User payee = this.userService.findById(transactionDTO.payeeId());

        this.userService.validateUser(payer, transactionDTO.amount());

        boolean isAuthorize = this.authorizationService.authorizeTransaction();

        if (!isAuthorize) {
            throw new UnauthorizedTransactionException("Transação não autorizada");
        }

        Transaction transaction = Transaction.builder()
                .payee(payee)
                .payer(payer)
                .amount(transactionDTO.amount())
                .timeAtTransaction(LocalDateTime.now())
                .build();


        payer.setBalance(payer.getBalance().subtract(transactionDTO.amount()));
        payee.setBalance(payee.getBalance().add(transactionDTO.amount()));

        var transactionSaved = transactionRepository.save(transaction);

        userService.updateBalance(payer);
        userService.updateBalance(payee);

        this.notificationService.sendNotifation(payee.getEmail());

        return transactionConverter.toTransactionResponseDTO(transactionSaved);
    }
}

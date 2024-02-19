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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final TransactionConverter transactionConverter;
    private final RestTemplate restTemplate;

    @Transactional
    public TransactionResponseDTO createTransaction(TransactionDTO transactionDTO) throws Exception {

        User payer = this.userService.findById(transactionDTO.payer());
        User payee = this.userService.findById(transactionDTO.payee());

        this.userService.validateUser(payer, transactionDTO.amount());

        boolean isAuthorize = this.authorizeTransaction();

        if (!isAuthorize) {
            throw new UnauthorizedTransactionException("Transação não autorizada");
        }

        this.notificationService.sendNotifation(payee.getEmail());

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

        return transactionConverter.toTransactionResponseDTO(transactionSaved);
    }

    public boolean authorizeTransaction() {
        String url = "https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc";

        var response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {

            Map<String, Object> responseBody = response.getBody();
            return responseBody.get("message").equals("Autorizado");

        }
        return false;
    }
}

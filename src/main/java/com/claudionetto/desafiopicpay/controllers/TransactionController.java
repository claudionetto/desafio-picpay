package com.claudionetto.desafiopicpay.controllers;

import com.claudionetto.desafiopicpay.dto.TransactionDTO;
import com.claudionetto.desafiopicpay.dto.TransactionResponseDTO;
import com.claudionetto.desafiopicpay.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) throws Exception {

        return new ResponseEntity<>(transactionService.createTransaction(transactionDTO), HttpStatus.OK);

    }

}

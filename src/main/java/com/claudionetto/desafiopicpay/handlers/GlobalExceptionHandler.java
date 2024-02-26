package com.claudionetto.desafiopicpay.handlers;

import com.claudionetto.desafiopicpay.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ExceptionResponse> handlerInsufficientBalanceException(
            InsufficientBalanceException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .title("Insufficient balance, check the documentation ")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .details(ex.getMessage())
                        .timeStamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handlerUserNotFoundException(
            UserNotFoundException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .title("User not found, check the documentation ")
                        .status(HttpStatus.NOT_FOUND.value())
                        .details(ex.getMessage())
                        .timeStamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build(), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MerchantCannotMakeTransactionsException.class)
    public ResponseEntity<ExceptionResponse> handlerMerchantCannotMakeTransactionsException(
            MerchantCannotMakeTransactionsException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .title("Merchant cannot make transactions, check the documentation ")
                        .status(HttpStatus.FORBIDDEN.value())
                        .details(ex.getMessage())
                        .timeStamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build(), HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(UnauthorizedTransactionException.class)
    public ResponseEntity<ExceptionResponse> handlerUnauthorizedTransactionException(
            UnauthorizedTransactionException ex, HttpServletRequest request) {

        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .title("Unauthorized transaction, check the documentation ")
                        .status(HttpStatus.FORBIDDEN.value())
                        .details(ex.getMessage())
                        .timeStamp(LocalDateTime.now())
                        .path(request.getRequestURI())
                        .build(), HttpStatus.FORBIDDEN
        );
    }
}

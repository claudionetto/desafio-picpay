package com.claudionetto.desafiopicpay.handlers;

import com.claudionetto.desafiopicpay.exceptions.ExceptionResponse;
import com.claudionetto.desafiopicpay.exceptions.InsufficientBalanceException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ExceptionResponse> handlerInsufficientBalanceException(
            InsufficientBalanceException ex, HttpServletRequest request){

        String title = "Insufficient Balance for this transaction";
        return response(title, ex.getMessage(), request, HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

    private ResponseEntity<ExceptionResponse> response(
            final String title, final String details, final HttpServletRequest request,
            final HttpStatus status, LocalDateTime date) {

        return ResponseEntity
                .status(status)
                .body(ExceptionResponse.builder()
                        .title(title)
                        .details(details)
                        .status(status.value())
                        .timeStamp(date)
                        .path(request.getRequestURI())
                        .build());

    }

}

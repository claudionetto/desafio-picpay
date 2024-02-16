package com.claudionetto.desafiopicpay.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class MerchantCannotMakeTransactionsException extends RuntimeException{
    public MerchantCannotMakeTransactionsException(String message){
        super(message);
    }
}

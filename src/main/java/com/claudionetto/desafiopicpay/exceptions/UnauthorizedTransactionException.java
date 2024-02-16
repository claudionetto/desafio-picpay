package com.claudionetto.desafiopicpay.exceptions;

public class UnauthorizedTransactionException extends RuntimeException{

    public UnauthorizedTransactionException(String message){
        super(message);
    }
}

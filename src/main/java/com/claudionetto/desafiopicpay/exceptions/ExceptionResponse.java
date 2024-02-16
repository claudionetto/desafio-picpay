package com.claudionetto.desafiopicpay.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionResponse {

    private String title;
    private String details;
    private int status;
    private LocalDateTime timeStamp;
    private String path;
}

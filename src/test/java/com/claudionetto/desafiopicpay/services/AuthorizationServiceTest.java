package com.claudionetto.desafiopicpay.services;

import com.claudionetto.desafiopicpay.exceptions.UnauthorizedTransactionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @InjectMocks
    AuthorizationService authorizationService;
    @Mock
    RestTemplate restTemplate;

    @Test
    @DisplayName("Should return true for authorized transaction when successful")
    void authorizeTransaction_ShouldReturnTrue_WhenTransactionAuthorized(){
        String url = "https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc";
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Autorizado");
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(url, Map.class)).thenReturn(responseEntity);

        boolean result = authorizationService.authorizeTransaction();

        Assertions.assertTrue(result);
        Mockito.verify(restTemplate, Mockito.times(1)).getForEntity(url, Map.class);
    }

    @Test
    @DisplayName("Should throw UnauthorizedTransactionException for unauthorized transaction")
    void authorizeTransaction_ShouldReturnUnauthorizedTransactionException_WhenTransactionUnauthorized(){
        String url = "https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc";
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Negado");
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        Mockito.when(restTemplate.getForEntity(url, Map.class)).thenReturn(responseEntity);

        Assertions.assertThrows(UnauthorizedTransactionException.class, () -> authorizationService.authorizeTransaction());
        Mockito.verify(restTemplate, Mockito.times(1)).getForEntity(url, Map.class);
    }

}
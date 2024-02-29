package com.claudionetto.desafiopicpay.services;

import com.claudionetto.desafiopicpay.exceptions.UnauthorizedTransactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final RestTemplate restTemplate;

    public boolean authorizeTransaction() {
        String url = "https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc";

        var response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {

            Map<String, Object> responseBody = response.getBody();

            if(responseBody != null && responseBody.get("message").equals("Autorizado")){
                return true;
            }
        }

        throw new UnauthorizedTransactionException("Transação não autorizada");
    }
}

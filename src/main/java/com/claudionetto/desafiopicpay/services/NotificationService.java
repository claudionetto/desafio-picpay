package com.claudionetto.desafiopicpay.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationService {

    private final RestTemplate restTemplate;

    public boolean sendNotification(String email) {
        String url = "https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6";

        try {
            Map<String, Boolean> notificationSent = restTemplate.getForObject(url, Map.class);

            if (notificationSent != null && notificationSent.get("message") != null) {

                boolean isNotificationSent = notificationSent.get("message");

                if (isNotificationSent) {
                    log.info("Email enviado para: " + email);
                    return true;
                }
            }
        } catch (RestClientException e) {
            log.error("Erro ao chamar o serviço de notificação: " + e.getMessage());
        }

        return false;
    }
}
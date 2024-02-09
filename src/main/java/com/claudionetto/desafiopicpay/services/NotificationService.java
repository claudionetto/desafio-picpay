package com.claudionetto.desafiopicpay.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RestTemplate restTemplate;

    public boolean sendNotifation(String email) {
        String url = "https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6";

        Map noticationSent = restTemplate.getForObject(url, Map.class);

        boolean isNotificationSent = Boolean.getBoolean(noticationSent.get("message").toString());

        if (isNotificationSent) {
            System.out.println("Email enviado para: " + email);
            return true;
        }

        return false;
    }

}

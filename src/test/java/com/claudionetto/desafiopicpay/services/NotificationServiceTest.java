package com.claudionetto.desafiopicpay.services;

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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    NotificationService notificationService;

    @Mock
    RestTemplate restTemplate;
    
    private final String EMAIL =  "claudio@gmail.com";
    private final String URL = "https://run.mocky.io/v3/54dc2cf1-3add-45b5-b5a9-6bf7e7f1f4a6";

    @Test
    @DisplayName("Should return true when notification is sent")
    void sendNotification_ShouldReturnTrue_WhenNotificationIsSent() {
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", true);
        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        Mockito.when(restTemplate.getForObject(this.URL, Map.class)).thenReturn(responseEntity.getBody());

        boolean result = notificationService.sendNotification(this.EMAIL);

        Assertions.assertTrue(result);
        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(this.URL, Map.class);
    }

    @Test
    @DisplayName("Should return false when notification isn't sent")
    void sendNotification_ShouldReturnFalse_WhenNotificationIsNotSent() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", false);
        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);

        Mockito.when(restTemplate.getForObject(this.URL, Map.class)).thenReturn(responseEntity.getBody());

        boolean result = notificationService.sendNotification(this.EMAIL);

        Assertions.assertFalse(result);
        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(this.URL, Map.class);
    }

    @Test
    @DisplayName("Should return false when notification response is invalid or null")
    void sendNotification_ShouldReturnFalse_WhenNotificationIsInvalid() {
        boolean result = notificationService.sendNotification(this.EMAIL);

        Assertions.assertFalse(result);
        Mockito.verify(restTemplate, Mockito.times(1)).getForObject(this.URL, Map.class);
    }

    @Test
    @DisplayName("Should return false when RestClientException occurs")
    void sendNotification_ShouldReturnFalse_WhenRestClientExceptionOccurs(){

        Mockito.when(restTemplate.getForObject(this.URL, Map.class)).thenThrow(new RestClientException("Simulated RestClientException"));
        boolean result = notificationService.sendNotification(this.EMAIL);

        Assertions.assertFalse(result);
    }

}
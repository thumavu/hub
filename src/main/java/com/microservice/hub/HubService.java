package com.microservice.hub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class HubService {

    private Map<String, String> routeMap = new HashMap<>(); // In-memory key-value store for routing

    @Autowired
    private RestTemplate restTemplate;

    public void pushFileToBank(String bankId, Bank.FileData fileData) {
        String bankRoute = routeMap.get(bankId);
        if (bankRoute != null) {
            // Convert the FileDataProto to bytes
            byte[] fileDataBytes = fileData.toByteArray();

            // Send the request to the bank interface
            sendRequestToBank(bankRoute, fileDataBytes);
        } else {
            // Handle invalid bankId or missing route
            throw new RuntimeException("Invalid bankID");
        }
    }

    public void registerBank(String bankId, Bank.BankInfo bankInfoProto) {
        String bankRoute = bankInfoProto.getBankHostname() + ":" + bankInfoProto.getBankPort();
        routeMap.put(bankId, bankRoute);
    }

    private void sendRequestToBank(String bankRoute, byte[] requestBytes) {
        // Create the HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // Create the HTTP entity with headers and request body
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(requestBytes, headers);

        // Build the bank interface URL
        String bankInterfaceUrl = "http://" + bankRoute + "/receiveFile";

        // Send the request and handle the response
        try {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    bankInterfaceUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Void.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // Request successful
                System.out.println("File successfully pushed to bank interface: " + bankRoute);
            } else {
                // Request failed
                System.out.println("Request to bank interface failed with status code: " + responseEntity.getStatusCode());
            }
        } catch (RestClientException e) {
            throw new RestClientException("Unable to establish connection");
        }
    }
}
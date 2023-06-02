package com.microservice.hub;

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

/**
 * The @HubService is responsible for handling business logic of the Hub.
 * It processes request received from the HubController
 *
 * */
@Service
public class HubService {

    private Map<String, String> routeMap = new HashMap<>();
    private RestTemplate restTemplate;

    public HubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * The @pushFileToBank() method takes in fileData and bankId.
     * It receives bank's route from routeMap , if valid route then converts FileData into byte array then calls send request.
     *
     * */
    public void pushFileToBank(String bankId, Bank.FileData fileData) {
        String bankRoute = routeMap.get(bankId);
        if (bankRoute != null) {
            // Convert the FileDataProto to bytes
            byte[] fileDataBytes = fileData.toByteArray();
            sendRequestToBank(bankRoute, fileDataBytes);
        } else {
            throw new RuntimeException("Invalid bankID");
        }
    }

    public void registerBank(String bankId, Bank.BankInfo bankInfoProto) {
        String bankRoute = bankInfoProto.getBankHostname() + ":" + bankInfoProto.getBankPort();
        routeMap.put(bankId, bankRoute);
    }

    /**
     * sendRequestToBank() tries to  send request to the bank if it fails, it throws and exception.
     * It creates HTTP headers and sets the content type to 'APPLICATION_OCTET_STREAM' which is a media type specification,
     * that represents binary data.
     * */
    private void sendRequestToBank(String bankRoute, byte[] requestBytes) {
        // Create the HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // Create the HTTP entity with headers and request body
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(requestBytes, headers);

        // Build the bank interface URL
        String bankInterfaceUrl = "http://" + bankRoute + "/receive";

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
package com.microservice.hub;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * The @HubController is responsible for handling incoming HTTP requests
 * and define endpoints that the clients(Banks) can interact with.
 *
 * */

// Annotations that define the class is a REST controller, and all endpoints will be under /hub
@RestController
@RequestMapping("/hub")
public class HubController {

    private final HubService hubService;

    public HubController(HubService hubService) {
        this.hubService = hubService;
    }

    /**
     * The @sendFileToBank() method handles HTTP POST requests to the specified endpoint.
     * It takes in bankID and fileDataProto as args.
     * @RequestBody represents deserialization into an object of type 'Bank.FileData' which is a protobuf generated class
     * and specify data structure of file sent
     *
     * If unable to push file to bank then an exception is thrown else return 'OK' response entity
     * */
    @PostMapping("/send-file/{bankId}")
    public ResponseEntity<String> sendFileToBank(@PathVariable String bankId, @RequestBody Bank.FileData fileDataProto) {
        hubService.pushFileToBank(bankId, fileDataProto);
        return ResponseEntity.ok("File pushed to bank: " + bankId);
    }

    /**
     * The @registerToBank()is another method that handles HTTP POST requests to the specified endpoint.
     * It takes in bankID and fileDataProto as args.
     * @RequestBody represents deserialization into an object of type 'Bank.BankInfo' which is a protobuf generated class.
     * Bank.BankInfo also specifies info such as bank's hostname and port
     * If unable to register bank then an exception is thrown else return 'OK' response entity
     * */
    @PostMapping("/registerBank/{bankId}")
    public ResponseEntity<String> registerBank(@PathVariable String bankId, @RequestBody Bank.BankInfo bankInfoProto) {
        hubService.registerBank(bankId, bankInfoProto);
        return ResponseEntity.ok("Bank registered: " + bankId);
    }

    @RequestMapping("/error")
    public ResponseEntity<String> handleErrors() {
        String errorMessage = "An error occurred while processing your request.";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}
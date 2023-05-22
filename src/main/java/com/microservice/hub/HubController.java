package com.microservice.hub;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hub")
public class HubController {

    private final HubService hubService;

    public HubController(HubService hubService) {
        this.hubService = hubService;
    }

    @PostMapping("/push-file/{bankId}")
    public ResponseEntity<String> pushFileToBank(@PathVariable String bankId, @RequestBody Bank.FileData fileDataProto) {
        hubService.pushFileToBank(bankId, fileDataProto);
        return ResponseEntity.ok("File pushed to bank: " + bankId);
    }

    @PostMapping("/registerBank/{bankId}")
    public ResponseEntity<String> registerBank(@PathVariable String bankId, @RequestBody Bank.BankInfo bankInfoProto) {
        hubService.registerBank(bankId, bankInfoProto);
        return ResponseEntity.ok("Bank registered: " + bankId);
    }
}